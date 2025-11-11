package id.co.edtslib.tracker.data

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackerUser (
    @SerialName("session_id")
    val sessionId: String?,
    @SerialName("user_id")
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