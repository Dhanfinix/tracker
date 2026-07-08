package id.co.edtslib.tracker_modern.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class TrackerUser (
    @SerializedName("session_id")
    val sessionId: String?,
    @SerializedName("user_id")
    val userId: Long?
){
    companion object {
        @SuppressLint("HardwareIds")
        fun create(sessionId: String?, userId: Long) : TrackerUser {
            return TrackerUser(
                sessionId, userId
            )
        }
    }
}