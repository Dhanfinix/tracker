package id.co.edtslib.tracker.di.manual

import android.app.Application
import id.co.edtslib.tracker.di.TrackerInteractor
import id.co.edtslib.tracker.di.TrackerUseCase
import id.co.edtslib.tracker.di.manual.RepositoryFactory.getTrackerRepository

object UseCaseFactory {
    private val interactor : TrackerUseCase? = null
    fun getTrackerUseCase(
        app: Application
    ): TrackerUseCase {
        return interactor ?: TrackerInteractor(getTrackerRepository(app))
    }
}