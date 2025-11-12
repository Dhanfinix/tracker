package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.Tracker
import java.util.*

data class TrackerImpressionCore(
    @SerializedName("event_name")
    val eventName: String,
    @SerializedName("event_timestamp")
    val eventTimeStamp: String,
    @SerializedName("pageview_id")
    val pageViewId: String,
    @SerializedName("event_id")
    val eventId: Long,
    @SerializedName("event_category")
    val eventCategory: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("impression_list")
    val impressionList: List<*>,
    @SerializedName("service")
    val service: String
) {
    companion object {
        fun create(eventId: Long, category: String, time: Long, data: List<*>, service: String) =
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