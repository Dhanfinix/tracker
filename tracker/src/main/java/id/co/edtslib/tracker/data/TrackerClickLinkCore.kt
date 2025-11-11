package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.*

@Serializable
data class TrackerClickLinkCore(
    @SerialName("event_name")
    val eventName: String,
    @SerialName("event_timestamp")
    val eventTimeStamp: String,
    @SerialName("pageview_id")
    val pageViewId: String,
    @SerialName("event_id")
    val eventId: Long,
    @SerialName("event_category")
    val eventCategory: String,
    @SerialName("link_label")
    val linkName: String,
    @SerialName("link_url")
    val linkUrl: String?,
    @SerialName("page_name")
    val pageName: String,
    @SerialName("details")
    val details: JsonElement?,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(
            eventId: Long,
            name: String,
            category: String? = null,
            url: String? = null,
            details: JsonElement? = null,
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