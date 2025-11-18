package id.co.edtslib.tracker.di

import androidx.lifecycle.asLiveData
import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerFilterDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TrackerController is a non-UI class responsible for coordinating tracking-related operations
 * such as session creation, user identification, and event logging.
 *
 * This class was previously implemented as a ViewModel (TrackerViewModel), but has been refactored
 * because it is not tied to any Android lifecycle component (Activity, Fragment, or ViewModelStoreOwner).
 *
 * ViewModels are designed to survive configuration changes and manage UI-related state.
 * Since TrackerController is used in background contexts and regular classes (e.g., Tracker),
 * it does not benefit from ViewModel lifecycle management and should not depend on it.
 *
 * By converting this to a plain class with constructor injection via Hilt,
 * we improve modularity, testability, and remove unnecessary lifecycle constraints.
 *
 * Dependencies such as TrackerUseCase are injected directly, and coroutine scope is managed manually
 * using CoroutineScope(Dispatchers.IO), ensuring background execution without lifecycle coupling.
 */

open class TrackerController @Inject constructor(
    private val trackerUseCase: TrackerUseCase
) {
    fun createSession() = trackerUseCase.createSession().asLiveData()

    fun setUserId(userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.setUserId(userId).collect()
        }
    }

    fun setLatLng(lat: Double?, lng: Double?) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.setLatLng(lat, lng).collect()
        }
    }

    fun setInstallReferer(installReferer: InstallReferer) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.setInstallReferer(installReferer).collect()
        }
    }

    fun getInstallReferer() = trackerUseCase.getInstallReferer()

    fun setService(service: String) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.setService(service).collect()
        }
    }

    fun getService() = trackerUseCase.getService()

    fun trackOpenApplication() {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackApplication("open_app").collect()
        }
    }

    fun trackResumeApplication() {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackApplication("resume_app").collect()
        }
    }

    fun trackMinimizeApplication() {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackApplication("minimize_app").collect()
        }
    }

    fun trackCloseApplication() {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackApplication("close_app").collect()
        }
    }

    fun trackPage(pageName: String, pageId: String, pageUrlPath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackPage(pageName, pageId, pageUrlPath).collect()
        }
    }

    fun trackPageDetail(detail: Any?) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackPageDetail(detail).collect()
        }
    }

    fun trackClick(
        name: String,
        category: String? = null,
        url: String? = null,
        details: Any? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                trackerUseCase.trackClick(name, category, url, details).collect()
            }
            catch (_: Error) {

            }
        }
    }

    fun trackFilters(filters: List<TrackerFilterDetail>, category: String = "") {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackFilters(filters, category).collect()
        }
    }

    fun trackSort(sortType: String) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackSort(sortType).collect()
        }
    }

    fun trackSubmission(
        name: String,
        category: String,
        status: Boolean,
        reason: String?,
        details: Any? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackSubmission(name, category, status, reason, details).collect()
        }
    }

    fun <S, T> trackImpression(
        category: String,
        time: Long,
        data: List<*>,
        mapper: ((data: S) -> T)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackImpression<S, T>(category, time, data, mapper).collect()
        }
    }

    fun trackDisplayedItems(data: MutableList<Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackDisplayedItems(data).collect()
        }
    }

    fun trackSearch(keyword: String, details: Any? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            trackerUseCase.trackSearch(keyword, details).collect()
        }
    }

    fun getData() = trackerUseCase.getData()

    fun getPriorPageName() = trackerUseCase.getPriorPageName()
}