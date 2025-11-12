package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerSubmissionCore(
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_label")
    val eventLabel: String,
    @SerializedName("event_category")
    val eventCategory: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("event_status")
    val eventStatus: String,
    @SerializedName("failed_reason")
    val eventFailedReason: String?,
    @SerializedName("details")
    val details: Any?,
    @SerializedName("service")
    val service: String
) {
    companion object {
        fun create(
            eventId: Long,
            label: String,
            category: String,
            status: Boolean,
            reason: String?,
            details: Any? = null,
            service: String
        ) = TrackerSubmissionCore(
                eventName = "event_submission",
                eventTimeStamp = Date().time.toString(),
                pageViewId = Tracker.currentPageId,
                eventId = eventId,
                eventLabel = label,
                eventCategory = category,
                pageName = Tracker.currentPageName,
                eventStatus = if (status) "success" else "failed",
                eventFailedReason = reason,
                details = details,
                service = service
            )
    }
}