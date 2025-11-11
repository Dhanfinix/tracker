package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TrackerFilterCore(
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
    @SerialName("filter_list")
    val list: List<TrackerFilterDetail>,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, list: List<TrackerFilterDetail>, category: String, service: String) =
            TrackerFilterCore(
                eventName = "user_filter",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                eventCategory = category,
                pageName = Tracker.currentPageName,
                list = list,
                service = service
            )
    }

}