package id.co.edtslib.tracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TrackerActivityCore (
    @SerialName("event_name")
    val eventName: String,
    @SerialName("event_timestamp")
    val eventTimeStamp: String,
    @SerialName("pageview_id")
    val pageViewId: String,
    @SerialName("event_id")
    val eventId: Long,
    @SerialName("activity_details")
    val activityDetails: String,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun createPageActivity(eventId: Long, eventName: String, pageViewId: String, service: String) =
            TrackerActivityCore(eventName = "app_activity",
                eventTimeStamp = Date().time.toString(), pageViewId = pageViewId,
                eventId = eventId, activityDetails = eventName, service = service)


    }

}