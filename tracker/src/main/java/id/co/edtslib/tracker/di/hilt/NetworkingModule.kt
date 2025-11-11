package id.co.edtslib.tracker.di.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.di.AuthInterceptor
import id.co.edtslib.tracker.di.UnsafeOkHttpClient
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {
    @Provides
    @Singleton
    @TrackerOkHttp
    fun provideTrackerOkHttp()= UnsafeOkHttpClient().get()

    @Provides
    @Singleton
    @TrackerRetrofit
    fun provideRetrofit(
        @TrackerOkHttp okHttpClient: OkHttpClient,
    ): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(Tracker.baseUrl)
            .client(okHttpClient
                .newBuilder()
                .addInterceptor(
                    AuthInterceptor(Tracker.token, Tracker.isLegacy)
                ).build()
            )
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .build()
    }

}