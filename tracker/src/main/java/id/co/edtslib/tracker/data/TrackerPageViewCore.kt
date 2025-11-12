package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class TrackerPageViewCore (
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("page_urlpath")
    val pageUrlPath: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("previous_page")
    val previousPage: String,
    @SerializedName("previous_page_urlpath")
    val previousPageUrlPath: String,
    @SerializedName("service")
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