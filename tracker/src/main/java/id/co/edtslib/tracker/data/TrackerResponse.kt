package id.co.edtslib.tracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackerResponse (
    @SerialName("request")
    val request: String,
    @SerialName("response")
    val response: String?
)