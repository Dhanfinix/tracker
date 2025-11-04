package id.co.edtslib.tracker.di.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.di.TrackerInteractor
import id.co.edtslib.tracker.di.TrackerRepository
import id.co.edtslib.tracker.di.TrackerUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorModule {
    @Provides
    @Singleton
    fun provideTrackerInteractor(
        trackerRepository: TrackerRepository
    ): TrackerUseCase = TrackerInteractor(trackerRepository)
}