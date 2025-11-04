package id.co.edtslib.tracker.di.hilt

import android.app.Application
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.data.TrackerApiService
import id.co.edtslib.tracker.data.TrackerConfig
import id.co.edtslib.tracker.data.TrackerRemoteDataSource
import id.co.edtslib.tracker.di.ConfigurationLocalSource
import id.co.edtslib.tracker.di.ITrackerRepository
import id.co.edtslib.tracker.di.TrackerLocalDataSource
import id.co.edtslib.tracker.di.TrackerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTrackerRemoteSource(
        apiService: TrackerApiService,
        config: TrackerConfig
    ) = TrackerRemoteDataSource(apiService, config)

    @Provides
    @Singleton
    fun provideTrackerLocalSource(
        @TrackerSharePref sharedPreferences: SharedPreferences,
        app: Application,
        config: TrackerConfig
    ) = TrackerLocalDataSource(sharedPreferences, app, config)

    @Provides
    @Singleton
    fun provideTrackerConfigLocalSource(
        @TrackerSharePref sharedPreferences: SharedPreferences,
        app: Application
    ) = ConfigurationLocalSource(sharedPreferences, app)

    @Provides
    @Singleton
    fun provideTrackerRepository(
        remoteDataSource: TrackerRemoteDataSource,
        localDataSource: TrackerLocalDataSource,
        configurationLocalSource: ConfigurationLocalSource
    ): ITrackerRepository =
        TrackerRepository(
            remoteDataSource,
            localDataSource,
            configurationLocalSource
        )
}