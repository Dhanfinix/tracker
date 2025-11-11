package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.*

@Serializable
data class TrackerDisplayedItemCore(
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
    @SerialName("item_list")
    val itemList: MutableList<JsonElement>,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, data: MutableList<JsonElement>, service: String) =
            TrackerDisplayedItemCore(
                eventName = "displayed_item",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                pageName = Tracker.currentPageName,
                itemList = data,
                service = service
            )
    }
}