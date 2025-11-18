package id.co.edtslib.tracker.di.manual

import com.google.gson.Gson
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.di.AuthInterceptor
import id.co.edtslib.tracker.di.UnsafeOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkingFactory {
    private val retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit = retrofit ?:
        Retrofit.Builder()
            .baseUrl(Tracker.baseUrl)
            .client(
                UnsafeOkHttpClient().get()
                    .newBuilder()
                    .addInterceptor(AuthInterceptor(Tracker.token, Tracker.isLegacy))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
}

