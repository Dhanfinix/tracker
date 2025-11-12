package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName

data class TrackerResponse (
    @SerializedName("request")
    val request: String,
    @SerializedName("response")
    val response: String?
)