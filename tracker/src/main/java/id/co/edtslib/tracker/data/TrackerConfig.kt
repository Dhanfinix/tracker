package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.di.AuthInterceptor

data class TrackerConfig(
    var baseUrl: String = "https://placeholder-tracker-url.com",
    var token: String = "",
    var path: String = "apps-tracker-gateway",
    var isLegacy: Boolean = false,
    var debugging: Boolean = false,
    var resend: Boolean = true,
    var appVersion: String = "1.0.0",
    var isSingleton: Boolean = true,
    var authInterceptor: AuthInterceptor? = null
)