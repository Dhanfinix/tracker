package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.*

@Serializable
data class TrackerSearchCore(
    @SerialName("event_name")
    val eventName: String,
    @SerialName("event_timestamp")
    val eventTimeStamp: String,
    @SerialName("pageview_id")
    val pageViewId: String,
    @SerialName("event_id")
    val eventId: Long,
    @SerialName("page_name")
    val pageName: String,
    @SerialName("search_input")
    val keyword: String,
    @SerialName("details")
    val details: JsonElement?,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, keyword: String, details: JsonElement? = null, service: String) =
            TrackerSearchCore(
                eventName = "user_search",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                pageName = Tracker.currentPageName,
                keyword = keyword,
                details = details,
                service = service
            )
    }
}