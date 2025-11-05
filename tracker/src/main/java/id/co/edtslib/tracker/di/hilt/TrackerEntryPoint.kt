package id.co.edtslib.tracker.di.hilt

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.Tracker

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TrackerEntryPoint {
    fun tracker(): Tracker
}
