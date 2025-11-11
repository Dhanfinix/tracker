package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.Date

@Serializable
data class TrackerPageDetailCore(
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
    @SerialName("details")
    val details: JsonElement?,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, details: JsonElement?, service: String) =
            TrackerPageDetailCore(
                eventName = "page_detail",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                pageName = Tracker.currentPageName,
                eventId = eventId,
                details = details,
                service = service
            )
    }
}