package id.co.edtslib.tracker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import id.co.edtslib.baserecyclerview.BaseRecyclerViewAdapter
import id.co.edtslib.baserecyclerview2.BaseRecyclerView2
import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerData
import id.co.edtslib.tracker.data.TrackerFilterDetail
import id.co.edtslib.tracker.di.ConfigurationLocalSource
import id.co.edtslib.tracker.di.TrackerDependencies
import java.util.Date
import java.util.WeakHashMap

class Tracker private constructor( //force to use builder
    context: Context,
    config: TrackerConfig
) {
    private var dependencies = TrackerDependencies(context, config)
    private var controller = dependencies.controller

    data class ImpressionData(
        val data: List<Any>,
        val time: Long
    )

    data class TrackerConfig(
        val baseUrl: String,
        val token: String,
        val path: String,
        val isLegacy: Boolean,
        val debugging: Boolean,
        val resend: Boolean,
        val appVersion: String
    )

    class Builder(context: Context) {
        private val appContext = context.applicationContext

        private var baseUrl: String = "https://placeholder-tracker-url.com"
        private var token: String = ""
        private var path: String = "apps-tracker-gateway"
        private var isLegacy: Boolean = false
        private var debugging: Boolean = false
        private var resend: Boolean = true
        private var appVersion: String = "1.0.0"
        private var isSingleton: Boolean = true

        fun setBaseUrl(url: String) = apply { this.baseUrl = url }
        fun setToken(token: String) = apply { this.token = token }
        fun setPath(path: String) = apply { this.path = path }
        fun setLegacy(isLegacy: Boolean) = apply { this.isLegacy = isLegacy }
        fun setDebugging(debug: Boolean) = apply { this.debugging = debug }
        fun setResend(resend: Boolean) = apply { this.resend = resend }
        fun setAppVersion(version: String) = apply { this.appVersion = version }
        fun setSingleton(isSingleton: Boolean) = apply { this.isSingleton = isSingleton }

        fun build(): Tracker {
            val config = TrackerConfig(
                baseUrl,
                token,
                path,
                isLegacy,
                debugging,
                resend,
                appVersion
            )
            val trackerInstance = Tracker(appContext, config)
            if (isSingleton) {
                synchronized(Tracker::class.java) {
                    // Only ONE thread can be inside these curly braces at a time.
                    setSingleton(trackerInstance)
                }
            }
            return trackerInstance
        }
    }

    companion object {
        @Volatile
        private var instance: Tracker? = null
            get() {
                if (field == null) {
                    // LOUD ERROR for the developer
                    Log.e(
                        "Tracker",
                        "❌ TRACKING SKIPPED: Tracker not initialized! Call Tracker.Builder(context).build() first."
                    )
                    return null
                }
                return field
            }

        private fun setSingleton(tracker: Tracker) {
            instance = tracker
        }

        /** don't set manual, set with resume fun */
        var currentPageName = ""
            private set
        /** don't set manual, set with resume fun */
        var currentPageId = ""
            private set

        /** The WeakHashMap will automatically remove the RecyclerView entry
         * when it's no longer referenced elsewhere and gets garbage collected.
         * This needed, because we encourage singleton Tracker instance.
         */
        private val impressionTracking = WeakHashMap<RecyclerView, Pair<Int, Int>>()

        fun getTrackerLocalConfig(): ConfigurationLocalSource? =
            instance?.dependencies?.configurationLocalSource

        fun getInstallReferer(){
            instance?.controller?.getInstallReferer()
        }

        fun checkInstallReferrer(activity: FragmentActivity) {
            val referrerClient = InstallReferrerClient.newBuilder(activity).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        when (responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                checkInstallReferrer(
                                    referrerClient.installReferrer?.installReferrer!!,
                                    activity.intent
                                )
                            }

                            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                                // API not available on the current Play Store app.
                            }

                            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                                // Connection couldn't be established.
                            }
                        }
                    } catch (ignore: RuntimeException) {

                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }

        fun checkInstallReferrer(utm_raw: String?, intent: Intent?) {
            if (intent?.data?.getQueryParameter("utm_source") != null) {
                instance?.controller?.setInstallReferer(InstallReferer(intent.data?.toString()))
            } else {
                instance?.controller?.setInstallReferer(InstallReferer(utm_raw))
            }
        }

        fun setUserId(userId: Long) {
            instance?.controller?.setUserId(userId)
        }

        fun setLatLng(lat: Double, lng: Double) {
            instance?.controller?.setLatLng(lat, lng)
        }

        fun getService() {
            instance?.controller?.getService()
        }

        fun setService(service: String) {
            instance?.controller?.setService(service)
        }

        fun trackPage(pageName: String, pageId: String, pageUrlPath: String = "") {
            instance?.controller?.trackPage(pageName, pageId, pageUrlPath)
            resumePage(pageName, pageId)
        }

        fun trackPageDetail(detail: Any?) {
            instance?.controller?.trackPageDetail(detail)
        }

        fun trackClick(
            name: String,
            category: String? = null,
            url: String? = null,
            details: Any? = null
        ) {
            instance?.controller?.trackClick(name, category, url, details)
        }

        fun trackFilters(filters: List<TrackerFilterDetail>, category: String = "") {
            instance?.controller?.trackFilters(filters, category)
        }

        fun trackSort(sortType: String) {
            instance?.controller?.trackSort(sortType)
        }

        fun trackSubmissionSuccess(name: String, category: String, details: Any? = null) {
            instance?.controller?.trackSubmission(name, category, true, "", details)
        }

        fun trackSubmissionFailed(
            name: String,
            category: String,
            reason: String?,
            details: Any? = null
        ) {
            instance?.controller?.trackSubmission(name, category, false, reason, details)
        }

        fun <S, T> trackImpression(
            category: String,
            data: List<*>,
            mapper: ((data: S) -> T)? = null
        ) {
            instance?.controller?.trackImpression<S, T>(category, Date().time, data, mapper)
        }

        fun <S, T> trackImpression(
            category: String,
            time: Long,
            data: List<*>,
            mapper: ((data: S) -> T)? = null
        ) {
            instance?.controller?.trackImpression<S, T>(category, time, data, mapper)
        }

        fun trackDisplayedItems(data: MutableList<Any>) {
            instance?.controller?.trackDisplayedItems(data)
        }

        fun trackSearch(keyword: String, details: Any? = null) {
            instance?.controller?.trackSearch(keyword, details)
        }

        fun trackOpenApplication() {
            val localController = instance?.controller ?: return
            val sessionLiveData = localController.createSession()
            val observer = object : androidx.lifecycle.Observer<Any?> {
                override fun onChanged(value: Any?) {
                    localController.trackOpenApplication()
                    sessionLiveData.removeObserver(this)
                }
            }
            sessionLiveData.observeForever(observer)
        }

        fun trackCloseApplication() {
            instance?.controller?.trackCloseApplication()
        }

        fun trackResumeApplication() {
            instance?.controller?.trackResumeApplication()
        }

        fun trackMinimizeApplication() {
            instance?.controller?.trackMinimizeApplication()
        }

        fun resumePage(pageName: String, pageId: String) {
            currentPageName = pageName
            currentPageId = pageId
        }

        fun getData(): TrackerData? = instance?.controller?.getData()

        /**
         * Returns the actual previous page name before the current one was tracked.
         *
         * Unlike [id.co.edtslib.tracker.di.ConfigurationLocalSource.getPreviousPageName],
         * which may reflect the current page due to overwrite during [id.co.edtslib.tracker.Tracker.trackPage],
         * this method preserves the last known page before tracking occurred.
         *
         * Useful for scenarios where page context is needed outside the tracking lifecycle,
         * such as in Sentinel.
         */
        fun getPriorPageName(): String? = instance?.controller?.getPriorPageName()

        fun <S, T> setImpressionRecyclerView(
            category: String,
            recyclerView: RecyclerView,
            mapper: ((data: S) -> T)? = null
        ) {
            // Initialize tracking state for this specific RecyclerView
            impressionTracking[recyclerView] = Pair(-1, -1)

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (recyclerView.tag != null) {
                            if (recyclerView.tag is List<*>) {
                                val list = recyclerView.tag as List<*>
                                list.forEach {
                                    if (it is ImpressionData) {
                                        trackImpression(
                                            category = category,
                                            time = it.time,
                                            data = it.data,
                                            mapper = mapper
                                        )
                                    }
                                }
                            }
                        }
                        recyclerView.tag = null
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if ((recyclerView.layoutManager is LinearLayoutManager || recyclerView.layoutManager is StaggeredGridLayoutManager) &&
                        (recyclerView.adapter is BaseRecyclerViewAdapter<*, *> || recyclerView.adapter is BaseRecyclerView2)) {

                        val first: Int
                        val last: Int
                        if (recyclerView.layoutManager is LinearLayoutManager) {
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            first = layoutManager.findFirstVisibleItemPosition()
                            last = layoutManager.findLastVisibleItemPosition()
                        } else {
                            val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                            first = layoutManager.findFirstVisibleItemPositions(null)[0]
                            last = layoutManager.findLastVisibleItemPositions(null)[0]
                        }

                        // Get tracking state for THIS RecyclerView
                        val (prevFirst, prevLast) = impressionTracking[recyclerView] ?: Pair(-1, -1)

                        if (prevFirst != first && prevLast != last) {
                            // Update tracking state for THIS RecyclerView
                            impressionTracking[recyclerView] = Pair(first, last)

                            if (recyclerView.adapter is BaseRecyclerViewAdapter<*, *>) {
                                addImpression(
                                    recyclerView,
                                    first,
                                    last,
                                    recyclerView.adapter as BaseRecyclerViewAdapter<*, *>
                                )
                            } else if (recyclerView.adapter is BaseRecyclerView2) {
                                addImpression(
                                    recyclerView,
                                    first,
                                    last,
                                    recyclerView.adapter as BaseRecyclerView2
                                )
                            }
                        }
                    }
                }
            })
        }

        private fun addImpression(
            recyclerView: RecyclerView,
            first: Int,
            end: Int,
            adapter: BaseRecyclerViewAdapter<*, *>
        ) {
            val l = mutableListOf<Any>()
            for (i in first until end + 1) {
                if (adapter.list.isNotEmpty()) {
                    val realPosition = i % adapter.list.size
                    if (realPosition >= 0 && realPosition < adapter.list.size) {
                        if (adapter.list[realPosition] != null) {
                            l.add(adapter.list[realPosition]!!)
                        }
                    }
                }
            }

            val newData = ImpressionData(data = l, time = Date().time)
            if (recyclerView.tag == null) {
                recyclerView.tag = listOf(newData)
            } else if (recyclerView.tag is List<*>) {
                val list = (recyclerView.tag as List<ImpressionData>).toMutableList()
                list.add(newData)
                recyclerView.tag = list
            }
        }

        private fun addImpression(
            recyclerView: RecyclerView,
            first: Int,
            end: Int,
            adapter: BaseRecyclerView2
        ) {
            val l = mutableListOf<Any>()
            for (i in first until end + 1) {
                if (adapter.list.isNotEmpty()) {
                    val realPosition = i % adapter.list.size
                    if (realPosition >= 0 && realPosition < adapter.list.size) {
                        if (adapter.list[realPosition].data != null) {
                            l.add(adapter.list[realPosition].data!!)
                        }
                    }
                }
            }

            val newData = ImpressionData(data = l, time = Date().time)
            if (recyclerView.tag == null) {
                recyclerView.tag = listOf(newData)
            } else if (recyclerView.tag is List<*>) {
                val list = (recyclerView.tag as List<ImpressionData>).toMutableList()
                list.add(newData)
                recyclerView.tag = list
            }
        }
    }
}