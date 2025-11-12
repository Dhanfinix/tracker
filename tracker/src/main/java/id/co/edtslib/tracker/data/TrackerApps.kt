package id.co.edtslib.tracker.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import com.google.gson.annotations.SerializedName
import id.co.edtslib.tracker.R
import java.util.*

data class TrackerApps (
    @SerializedName("os_name")
    val osName: String,
    @SerializedName("os_version")
    val osVersion: String,
    @SerializedName("device_class")
    val deviceClassName: String,
    @SerializedName("device_family")
    val deviceFamilyName: String,
    @SerializedName("os_timezone")
    val osTimeZone: String,
    @SerializedName("app_version")
    val appVersion: String,
    @SerializedName("device_id")
    val deviceId: String?,
    @SerializedName("source_name")
    val sourceName: String?

) {
    companion object {
        @SuppressLint("HardwareIds")
        fun create(
            context: Context?,
            appVersion: String
        ) : TrackerApps {
            val fields = Build.VERSION_CODES::class.java.fields

            var osName = "Android UNKNOWN"
            fields.filter { it.getInt(Build.VERSION_CODES::class) == Build.VERSION.SDK_INT }
                .forEach { osName = String.format("Android %s", it.name) }

            val osCode = String.format("Android %s", Build.VERSION.RELEASE)
            val className = try {
                if (context == null) "Phone" else if (context.resources.getBoolean(R.bool.isTablet)) "Tablet" else "Phone"
            }
            catch (e: Resources.NotFoundException) {
                "Phone"
            }

            val regex = Regex("[^A-Za-z0-9_ /.]")
            val familyName = regex.replace(String.format("%s %s", Build.MANUFACTURER, Build.MODEL), "")
            var deviceID = if (context == null) null else Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (deviceID == null) {
                deviceID = UUID.randomUUID().toString()
            }

            return TrackerApps(osName = osName,
                osVersion = osCode,
                deviceClassName = className,
                deviceFamilyName = familyName,
                appVersion = appVersion,
                deviceId = deviceID,
                sourceName = "apps",
                osTimeZone = TimeZone.getDefault().id)
        }
    }
}