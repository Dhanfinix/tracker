package id.co.edtslib.tracker

import android.content.Intent
import android.util.Patterns
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
import id.co.edtslib.tracker.util.toSafeJsonElement
import kotlinx.serialization.json.JsonElement
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
        val data: List<JsonElement>,
        val time: Long
    )

    private var firstImpression = -1
    private var lastImpression = -1

    companion object {
        const val PLACEHOLDER_TRACKER_URL = "https://placeholder-tracker-url.com"

        var isInitialized: Boolean = false
            private set

        var baseUrl = PLACEHOLDER_TRACKER_URL
            private set
        var token = ""
            private set
        var path = "apps-tracker-gateway"
            private set
        var isLegacy = false
            private set

        var debugging = false
        var resend = true
        var appVersion = "1.0.0"

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
         */
        fun init(
            baseUrl: String,
            token: String,
            path: String = "apps-tracker-gateway",
            isLegacy: Boolean = false,
        ) {
            this.baseUrl = baseUrl
            this.token = token
            this.path = path
            this.isLegacy = isLegacy
            isInitialized = true
        }
    }

    /**
     * Verifies that the tracker has been initialized and that the configuration is valid before
     * executing a given action. This method acts as a guard to prevent tracking calls from being
     * made prematurely or with invalid settings.
     *
     * If initialization is incomplete or the configuration is invalid (e.g., placeholder or
     * malformed `baseUrl`), a warning is logged to Logcat when [debugging] is enabled. The
     * provided `onInitialized` lambda is only executed if the tracker is fully initialized and
     * configured correctly.
     *
     * @param functionName The name of the calling function, used for logging purposes.
     * @param onInitialized A high-order function to be executed only if the tracker is properly
     *                      initialized and configured.
     */
    fun checkInitialization(
        functionName: String,
        onInitialized: () -> Unit
    ) {
        val isValidUrl = Patterns.WEB_URL.matcher(baseUrl).matches()
        if (!isInitialized && debugging) {
            android.util.Log.w("Tracker", "$functionName called before init()")
        } else if (
            baseUrl == PLACEHOLDER_TRACKER_URL || 
            !isValidUrl || 
            baseUrl.isEmpty()
        ) {
            android.util.Log.w(
                "Tracker",
                "$functionName called with invalid baseUrl, please check your init()"
            )
        } else if (isInitialized) {
            onInitialized()
        }
    }

    fun getInstallReferer(){
        checkInitialization("getInstallReferer"){
            controller.getInstallReferer()
        }
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
        checkInitialization("checkInstallReferrer"){
            if (intent?.data?.getQueryParameter("utm_source") != null) {
                controller.setInstallReferer(InstallReferer(intent.data?.toString()))
            } else {
                controller.setInstallReferer(InstallReferer(utm_raw))
            }
        }
    }

    fun setUserId(userId: Long) {
        checkInitialization("setUserId"){
            controller.setUserId(userId)
        }
    }

    fun setLatLng(lat: Double, lng: Double) {
        checkInitialization("setLatLng"){
            controller.setLatLng(lat, lng)
        }
    }

    fun getService() {
        checkInitialization("getService"){
            controller.getService()
        }
    }

    fun setService(service: String) {
        checkInitialization("setService"){
            controller.setService(service)
        }
    }

    fun trackPage(pageName: String, pageId: String, pageUrlPath: String = "") {
        checkInitialization("trackPage"){
            controller.trackPage(pageName, pageId, pageUrlPath)
            resumePage(pageName, pageId)
        }
    }

    inline fun <reified T> trackPageDetail(detail: T?) {
        checkInitialization("trackPageDetail"){
            controller.trackPageDetail(detail.toSafeJsonElement())
        }
    }

    inline fun <reified T> trackClick(
        name: String,
        category: String? = null,
        url: String? = null,
        details: T? = null
    ) {
        checkInitialization("trackClick"){
            controller.trackClick(name, category, url, details.toSafeJsonElement())
        }
    }

    fun trackFilters(filters: List<TrackerFilterDetail>, category: String = "") {
        checkInitialization("trackFilters"){
            controller.trackFilters(filters, category)
        }
    }

    fun trackSort(sortType: String) {
        checkInitialization("trackSort"){
            controller.trackSort(sortType)
        }
    }

    inline fun <reified T> trackSubmissionSuccess(
        name: String,
        category: String,
        details: T? = null
    ) {
        checkInitialization("trackSubmissionSuccess"){
            controller.trackSubmission(name, category, true, "", details.toSafeJsonElement())
        }
    }

    inline fun <reified T> trackSubmissionFailed(
        name: String,
        category: String,
        reason: String?,
        details: T? = null
    ) {
        checkInitialization("trackSubmissionFailed"){
            controller.trackSubmission(name, category, false, reason, details.toSafeJsonElement())
        }
    }

    fun trackImpression(
        category: String,
        data: List<JsonElement>,
        mapper: ((data: JsonElement) -> JsonElement?)? = null
    ) {
        checkInitialization("trackImpression"){
            controller.trackImpression(category, Date().time, data, mapper)
        }
    }

    fun trackImpression(
        category: String,
        time: Long,
        data: List<JsonElement>,
        mapper: ((data: JsonElement) -> JsonElement?)? = null
    ) {
        checkInitialization("trackImpression"){
            controller.trackImpression(category, time, data, mapper)
        }
    }

    inline fun <reified T> trackDisplayedItems(data: MutableList<T>) {
        checkInitialization("trackDisplayedItems"){
            val newData = mutableListOf<JsonElement>()
            data.forEach {
                it.toSafeJsonElement()?.let { element -> newData.add(element) }
            }
            controller.trackDisplayedItems(newData)
        }
    }

    inline fun <reified T> trackSearch(keyword: String, details: T? = null) {
        checkInitialization("trackSearch"){
            controller.trackSearch(keyword, details.toSafeJsonElement())
        }
    }

    fun trackOpenApplication() {
        checkInitialization("trackOpenApplication"){
            controller.createSession().observeForever {
                controller.trackOpenApplication()
            }
        }
    }

    fun trackCloseApplication() {
        checkInitialization("trackCloseApplication"){
            controller.trackCloseApplication()
        }
    }

    fun trackResumeApplication() {
        checkInitialization("trackResumeApplication"){
            controller.trackResumeApplication()
        }
    }

    fun trackMinimizeApplication() {
        checkInitialization("trackMinimizeApplication"){
            controller.trackMinimizeApplication()
        }
    }

    fun resumePage(pageName: String, pageId: String) {
        currentPageName = pageName
        currentPageId = pageId
    }

    fun getData(): TrackerData? {
        var data : TrackerData? = null
        checkInitialization("trackMinimizeApplication"){
            data = controller.getData()
        }
        return data
    }

    fun setImpressionRecyclerView(
        category: String,
        recyclerView: RecyclerView,
        mapper: ((data: JsonElement) -> JsonElement?)? = null
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

    private fun <T> addImpression(
        recyclerView: RecyclerView,
        first: Int,
        end: Int,
        adapter: BaseRecyclerViewAdapter<*, T>
    ) {
        val l = mutableListOf<JsonElement>()
        for (i in first until end + 1) {
            if (adapter.list.isNotEmpty()) {
                val realPosition = i % adapter.list.size
                if (realPosition >= 0 && realPosition < adapter.list.size) {
                    val item = adapter.list[realPosition]
                    item?.toSafeJsonElement()?.let {
                        l.add(it)
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
        val l = mutableListOf<JsonElement>()
        for (i in first until end + 1) {
            if (adapter.list.isNotEmpty()) {
                val realPosition = i % adapter.list.size
                if (realPosition >= 0 && realPosition < adapter.list.size) {
                    val item = adapter.list[realPosition].data
                    item?.toSafeJsonElement()?.let {
                        l.add(it)
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