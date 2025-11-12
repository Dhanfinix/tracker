package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerClickLinkCore(
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_category")
    val eventCategory: String,
    @SerializedName("link_label")
    val linkName: String,
    @SerializedName("link_url")
    val linkUrl: String?,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("details")
    val details: Any?,
    @SerializedName("service")
    val service: String
) {
    companion object {
        fun create(
            eventId: Long,
            name: String,
            category: String? = null,
            url: String? = null,
            details: Any? = null,
            service: String
        ) = TrackerClickLinkCore(
            eventName = "click_link",
            eventTimeStamp = Date().time.toString(),
            pageViewId = Tracker.currentPageId,
            eventId = eventId,
            eventCategory = category ?: "",
            linkName = name,
            linkUrl = url,
            pageName = Tracker.currentPageName,
            details = details,
            service = service
        )
    }
}