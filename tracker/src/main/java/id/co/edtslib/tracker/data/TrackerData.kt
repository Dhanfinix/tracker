package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName

data class TrackerData(
    @SerializedName("core")
    val core: Any,
    @SerializedName("user")
    val user: TrackerUser?,
    @SerializedName("application")
    val application: TrackerApps?,
    @SerializedName("network")
    val network: TrackerNetwork?,
    @SerializedName("marketing")
    val marketing: InstallReferer?
)