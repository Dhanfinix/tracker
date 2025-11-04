package id.co.edtslib.tracker.di.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.data.TrackerApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainAppModule {
    @Provides
    @Singleton
    fun provideTrackerApiService(
        @TrackerRetrofit retrofit: Retrofit
    ): TrackerApiService = retrofit.create(TrackerApiService::class.java)
}