package id.co.edtslib.tracker.di

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */
class AuthInterceptor(private val token: String, private val isLegacy: Boolean) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                if (isLegacy) {
                    addHeader("Authorization", token)
                } else {
                    addHeader("x-api-key", token)
                }
            }
            .build()
        return chain.proceed(request)
    }
}