package id.co.edtslib.tracker_modern.di

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */
open class AuthInterceptor(
    protected open val token: String,
    protected open val isLegacy: Boolean
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        addHeader(requestBuilder)
        return chain.proceed(requestBuilder.build())
    }

    protected open fun addHeader(builder: Request.Builder) {
        if (isLegacy) {
            builder.addHeader("Authorization", token)
        } else {
            builder.addHeader("x-api-key", token)
        }
    }
}