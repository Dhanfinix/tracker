package id.co.edtslib.tracker.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.util.ConnectivityUtil

data class TrackerNetwork (
    @SerializedName("ip_address")
    val ipAddress: String?,
    @SerializedName("network_isp")
    val networkIsp: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("zipcode")
    val zipcode: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?

) {
    companion object {
        @SuppressLint("HardwareIds")
        fun create(latitude: Double?, longitude: Double?) : TrackerNetwork {
            return TrackerNetwork(ipAddress = ConnectivityUtil.getIPAddress(true),
                latitude = latitude,
                longitude = longitude,
                zipcode = null,
                city = null,
                country = null,
                networkIsp = null)
        }
    }
}