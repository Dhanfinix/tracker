package id.co.edtslib.tracker_modern.data

import com.google.gson.annotations.SerializedName

data class TrackerResponse (
    @SerializedName("request")
    val request: String,
    @SerializedName("response")
    val response: String?
)