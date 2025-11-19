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
    private var repository: ITrackerRepository? = null
    private var remoteDataSource: TrackerRemoteDataSource? = null
    private var localDataSource: TrackerLocalDataSource? = null
    private var configLocalDataSource: ConfigurationLocalSource? = null

    private fun getTrackerRemoteSource(): TrackerRemoteDataSource {
        return remoteDataSource ?: TrackerRemoteDataSource(
            getTrackerApiService()
        ).also { remoteDataSource = it }
    }

    private fun getTrackerLocalDataSource(app: Application): TrackerLocalDataSource {
        return localDataSource ?: TrackerLocalDataSource(
            getSharedPrefs(app), app
        ).also { localDataSource = it }
    }

    fun getTrackerConfigLocalDataSource(app: Application): ConfigurationLocalSource {
        return configLocalDataSource ?: ConfigurationLocalSource(
            getSharedPrefs(app), app
        ).also { configLocalDataSource = it }
    }


    fun getTrackerRepository(
        app: Application
    ): ITrackerRepository {
        return repository ?: TrackerRepository(
            getTrackerRemoteSource(),
            getTrackerLocalDataSource(app),
            getTrackerConfigLocalDataSource(app)
        ).also { repository = it }
    }
}