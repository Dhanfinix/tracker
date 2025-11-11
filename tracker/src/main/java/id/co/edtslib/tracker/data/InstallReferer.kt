package id.co.edtslib.tracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstallReferer(
    @SerialName("utm_raw")
    val utm_raw: String?
)