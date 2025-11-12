package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName

data class TrackerDataList (
    @SerializedName("data")
    val data: MutableList<TrackerData>
)