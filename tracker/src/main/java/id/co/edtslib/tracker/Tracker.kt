package id.co.edtslib.tracker

import android.content.Intent
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
import id.co.edtslib.tracker.di.TrackerController
import java.util.Date
import javax.inject.Inject

/**
 * Tracker is a lifecycle-safe, Hilt-managed analytics coordinator responsible for configuring
 * and delegating tracking operations to the injected [TrackerController].
 *
 * This class replaces the previous static or ViewModel-based implementation, ensuring proper
 * dependency injection, modularity, and testability. It is designed to be initialized once
 * during application startup via [init], and then injected wherever needed.
 *
 * ## Configuration
 * The [init] method sets up runtime parameters such as API endpoint, token, and behavior flags.
 * All configuration fields are exposed as read-only properties to prevent accidental mutation.
 *
 * ## Usage
 * - Call [init] once during app startup (e.g., in `Application.onCreate()`).
 * - Inject [Tracker] into any Hilt-aware component (Activity, Fragment, Service, etc.).
 * - Use instance methods to perform tracking operations via the controller.
 *
 * ## Properties
 * - [baseUrl], [token], [path], [isLegacy], [resend], [appVersion]: runtime configuration
 * - [debugging]: enables logging for development and diagnostics
 * - [currentPageName], [currentPageId]: updated internally during page tracking
 * - [isInitialized]: guards against premature usage
 *
 * ## Internal State
 * - [firstImpression], [lastImpression]: used for impression tracking boundaries
 *
 * @param controller The injected [TrackerController] that handles actual tracking logic.
 */
class Tracker @Inject constructor() {
    @Inject
    lateinit var controller: TrackerController

    data class ImpressionData(
        val data: List<Any>,
        val time: Long
    )

    private var firstImpression = -1
    private var lastImpression = -1

    companion object {
        var isInitialized: Boolean = false
            private set

        var baseUrl = ""
            private set

        var token = ""
            private set

        var path = "apps-tracker-gateway"
            private set

        var isLegacy = false
            private set

        var debugging = false
            private set

        var resend = true
            private set

        var appVersion = "1.0.0"
            private set

        /** don't set manual, set with resume fun */
        var currentPageName = ""
            private set
        /** don't set manual, set with resume fun */
        var currentPageId = ""
            private set

        /**
         * Initializes the tracker with runtime configuration.
         * Must be called once before using any tracking methods.
         *
         * @param baseUrl The base URL for the tracking API.
         * @param token The authentication token for API access.
         * @param path Optional path segment for the tracking endpoint.
         * @param isLegacy Whether to enable legacy tracking behavior.
         * @param resend Whether to allow resending failed events.
         * @param appVersion The current app version for tracking context.
         */
        fun init(
            baseUrl: String,
            token: String,
            path: String = "apps-tracker-gateway",
            isLegacy: Boolean = false,
            resend: Boolean = true,
            debugging: Boolean = false,
            appVersion: String = "1.0.0"
        ) {
            this.baseUrl = baseUrl
            this.token = token
            this.path = path
            this.isLegacy = isLegacy
            this.resend = resend
            this.debugging = debugging
            this.appVersion = appVersion
            isInitialized = true
        }
    }

    fun getInstallReferer() = controller.getInstallReferer()

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
            controller.setInstallReferer(InstallReferer(intent.data?.toString()))
        } else {
            controller.setInstallReferer(InstallReferer(utm_raw))
        }
    }

    fun setUserId(userId: Long) {
        controller.setUserId(userId)
    }

    fun setLatLng(lat: Double, lng: Double) {
        controller.setLatLng(lat, lng)
    }

    fun getService() = controller.getService()

    fun setService(service: String) {
        controller.setService(service)
    }

    fun trackPage(pageName: String, pageId: String, pageUrlPath: String = "") {
        controller.trackPage(pageName, pageId, pageUrlPath)
        resumePage(pageName, pageId)
    }

    fun trackPageDetail(detail: Any?) {
        controller.trackPageDetail(detail)
    }

    fun trackClick(
        name: String,
        category: String? = null,
        url: String? = null,
        details: Any? = null
    ) {
        controller.trackClick(name, category, url, details)
    }

    fun trackFilters(filters: List<TrackerFilterDetail>, category: String = "") {
        controller.trackFilters(filters, category)
    }

    fun trackSort(sortType: String) {
        controller.trackSort(sortType)
    }

    fun trackSubmissionSuccess(name: String, category: String, details: Any? = null) {
        controller.trackSubmission(name, category, true, "", details)
    }

    fun trackSubmissionFailed(
        name: String,
        category: String,
        reason: String?,
        details: Any? = null
    ) {
        controller.trackSubmission(name, category, false, reason, details)
    }

    fun <S, T> trackImpression(
        category: String,
        data: List<*>,
        mapper: ((data: S) -> T)? = null
    ) {
        controller.trackImpression<S, T>(category, Date().time, data, mapper)
    }

    fun <S, T> trackImpression(
        category: String,
        time: Long,
        data: List<*>,
        mapper: ((data: S) -> T)? = null
    ) {
        controller.trackImpression<S, T>(category, time, data, mapper)
    }

    fun trackDisplayedItems(data: MutableList<Any>) {
        controller.trackDisplayedItems(data)
    }

    fun trackSearch(keyword: String, details: Any? = null) {
        controller.trackSearch(keyword, details)
    }

    fun trackOpenApplication() {
        controller.createSession()?.observeForever {
            controller.trackOpenApplication()
        }
    }

    fun trackCloseApplication() {
        controller.trackCloseApplication()
    }

    fun trackResumeApplication() {
        controller.trackResumeApplication()
    }

    fun trackMinimizeApplication() {
        controller.trackMinimizeApplication()
    }

    fun resumePage(pageName: String, pageId: String) {
        currentPageName = pageName
        currentPageId = pageId
    }

    fun getData(): TrackerData? = controller.getData()

    fun <S, T> setImpressionRecyclerView(
        category: String,
        recyclerView: RecyclerView,
        mapper: ((data: S) -> T)? = null
    ) {
        firstImpression = -1
        lastImpression = -1

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

                if ((recyclerView.layoutManager is LinearLayoutManager || recyclerView.layoutManager is StaggeredGridLayoutManager) && (recyclerView.adapter is BaseRecyclerViewAdapter<*, *> || recyclerView.adapter is BaseRecyclerView2)) {

                    val first: Int
                    val last: Int
                    if (recyclerView.layoutManager is LinearLayoutManager) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        first = layoutManager.findFirstVisibleItemPosition()
                        last = layoutManager.findLastVisibleItemPosition()
                    } else {
                        val layoutManager =
                            recyclerView.layoutManager as StaggeredGridLayoutManager
                        first = layoutManager.findFirstVisibleItemPositions(null)[0]
                        last = layoutManager.findLastVisibleItemPositions(null)[0]
                    }

                    if (firstImpression != first && lastImpression != last) {
                        firstImpression = first
                        lastImpression = last

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