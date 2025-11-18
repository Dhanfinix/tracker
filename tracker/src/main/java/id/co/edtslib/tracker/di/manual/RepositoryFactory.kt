package id.co.edtslib.tracker.di.manual

import android.app.Application
import id.co.edtslib.tracker.data.TrackerRemoteDataSource
import id.co.edtslib.tracker.di.ConfigurationLocalSource
import id.co.edtslib.tracker.di.ITrackerRepository
import id.co.edtslib.tracker.di.TrackerLocalDataSource
import id.co.edtslib.tracker.di.TrackerRepository
import id.co.edtslib.tracker.di.manual.MainAppFactory.getTrackerApiService
import id.co.edtslib.tracker.di.manual.SharedPrefFactory.getSharedPrefs

object RepositoryFactory {
    private fun getTrackerRemoteSource(): TrackerRemoteDataSource {
        return TrackerRemoteDataSource(getTrackerApiService())
    }

    private fun getTrackerLocalDataSource(app: Application): TrackerLocalDataSource {
        return TrackerLocalDataSource(getSharedPrefs(app), app)
    }

    fun getTrackerConfigLocalDataSource(app: Application): ConfigurationLocalSource {
        return ConfigurationLocalSource(getSharedPrefs(app), app)
    }

    private var repository: ITrackerRepository? = null

    fun getTrackerRepository(
        app: Application
    ): ITrackerRepository {
        return repository ?: TrackerRepository(
            getTrackerRemoteSource(),
            getTrackerLocalDataSource(app),
            getTrackerConfigLocalDataSource(app)
        )
    }
}