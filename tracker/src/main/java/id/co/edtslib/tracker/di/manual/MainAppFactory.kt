package id.co.edtslib.tracker.di.manual

import id.co.edtslib.tracker.data.TrackerApiService
import id.co.edtslib.tracker.di.manual.NetworkingFactory.getRetrofit

object MainAppFactory {
    private var apiService: TrackerApiService? = null

    fun getTrackerApiService(): TrackerApiService{
        return apiService ?: getRetrofit().create(TrackerApiService::class.java)
            .also { apiService = it }
    }
}