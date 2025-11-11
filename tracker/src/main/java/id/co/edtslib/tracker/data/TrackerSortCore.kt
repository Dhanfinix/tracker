package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TrackerSortCore(
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
    @SerialName("sort_type")
    val sortType: String,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, sortType: String, service: String) =
            TrackerSortCore(
                eventName = "user_sort",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                pageName = Tracker.currentPageName,
                sortType = sortType,
                service = service
            )

    }

}