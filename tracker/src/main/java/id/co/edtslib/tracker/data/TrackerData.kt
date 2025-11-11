package id.co.edtslib.tracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class TrackerData(
    @SerialName("core")
    val core: JsonElement,
    @SerialName("user")
    val user: TrackerUser?,
    @SerialName("application")
    val application: TrackerApps?,
    @SerialName("network")
    val network: TrackerNetwork?,
    @SerialName("marketing")
    val marketing: InstallReferer?
)