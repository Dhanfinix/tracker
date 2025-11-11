package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class TrackerImpressionCore(
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
    @SerialName("page_name")
    val pageName: String,
    @SerialName("impression_list")
    val impressionList: List<JsonElement>,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, category: String, time: Long, data: List<JsonElement>, service: String) =
            TrackerImpressionCore(
                eventName = "user_impression",
                eventTimeStamp = time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                eventCategory = category,
                pageName = Tracker.currentPageName,
                impressionList = data,
                service = service
            )
    }
}