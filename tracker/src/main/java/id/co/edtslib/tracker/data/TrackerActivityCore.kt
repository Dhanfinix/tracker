package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class TrackerActivityCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("activity_details")
    val activityDetails: String,
    @SerializedName("service")
    val service: String
) {
    companion object {
        fun createPageActivity(eventId: Long, eventName: String, pageViewId: String, service: String) =
            TrackerActivityCore(eventName = "app_activity",
                eventTimeStamp = Date().time.toString(), pageViewId = pageViewId,
                eventId = eventId, activityDetails = eventName, service = service)


    }

}