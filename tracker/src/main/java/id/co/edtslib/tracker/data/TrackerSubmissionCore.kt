package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.util.*

@Serializable
data class TrackerSubmissionCore(
    @SerialName("event_name")
    val eventName: String,
    @SerialName("event_timestamp")
    val eventTimeStamp: String,
    @SerialName("pageview_id")
    val pageViewId: String,
    @SerialName("event_id")
    val eventId: Long,
    @SerialName("event_label")
    val eventLabel: String,
    @SerialName("event_category")
    val eventCategory: String,
    @SerialName("page_name")
    val pageName: String,
    @SerialName("event_status")
    val eventStatus: String,
    @SerialName("failed_reason")
    val eventFailedReason: String?,
    @SerialName("details")
    val details: JsonElement?,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(
            eventId: Long,
            label: String,
            category: String,
            status: Boolean,
            reason: String?,
            details: JsonElement? = null,
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