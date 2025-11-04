package id.co.edtslib.tracker.data

data class TrackerConfig(
    val baseUrl: String,
    val token: String,
    val path: String = "apps-tracker-gateway",
    val isLegacy: Boolean = false,
    val resend: Boolean = true,
    val debugging: Boolean = false,
    val appVersion: String = "1.0.0"
)
