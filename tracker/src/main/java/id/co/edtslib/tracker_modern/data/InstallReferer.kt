package id.co.edtslib.tracker_modern.data

import com.google.gson.annotations.SerializedName

data class InstallReferer(
    @SerializedName("utm_raw")
    val utm_raw: String?
)