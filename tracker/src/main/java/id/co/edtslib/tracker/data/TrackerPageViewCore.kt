package id.co.edtslib.tracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TrackerPageViewCore (
    @SerialName("event_name")
    val eventName: String,
    @SerialName("event_id")
    val eventId: Long,
    @SerialName("event_timestamp")
    val eventTimeStamp: String,
    @SerialName("page_urlpath")
    val pageUrlPath: String,
    @SerialName("page_name")
    val pageName: String,
    @SerialName("pageview_id")
    val pageViewId: String,
    @SerialName("previous_page")
    val previousPage: String,
    @SerialName("previous_page_urlpath")
    val previousPageUrlPath: String,
    @SerialName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, pageName: String, pageId: String, previousPage: String, pageUrlPath: String, prevPageUrlPath: String, service: String) =
            TrackerPageViewCore(
                eventName = "page_view",
                eventTimeStamp = Date().time.toString(),
                pageUrlPath = pageUrlPath,
                pageName = pageName,
                eventId = eventId,
                pageViewId = pageId,
                previousPage = previousPage,
                previousPageUrlPath = prevPageUrlPath,
                service = service
            )
        }
}