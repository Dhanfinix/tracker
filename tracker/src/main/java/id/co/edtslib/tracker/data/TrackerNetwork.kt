package id.co.edtslib.tracker.data

import android.annotation.SuppressLint
import id.co.edtslib.tracker.util.ConnectivityUtil
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackerNetwork (
    @SerialName("ip_address")
    val ipAddress: String?,
    @SerialName("network_isp")
    val networkIsp: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("city")
    val city: String?,
    @SerialName("zipcode")
    val zipcode: String?,
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
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