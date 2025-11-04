package id.co.edtslib.tracker.di.hilt

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.di.AuthInterceptor
import id.co.edtslib.tracker.di.UnsafeOkHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    @TrackerRetrofit
    fun provideRetrofit(
        @TrackerOkHttp okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Tracker.baseUrl)
        .client(okHttpClient.newBuilder().addInterceptor(AuthInterceptor(Tracker.token, Tracker.isLegacy)).build())
        .addConverterFactory(converterFactory)
        .build()

}