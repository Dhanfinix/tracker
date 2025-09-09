package id.co.edtslib.tracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerFilterDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class TrackerViewModel(
    private val trackerUseCase: TrackerUseCase
) : ViewModel() {
    fun createSession() = trackerUseCase.createSession().asLiveData()

    fun setUserId(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.setUserId(userId).collect()
        }
    }

    fun setLatLng(lat: Double?, lng: Double?) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.setLatLng(lat, lng).collect()
        }
    }

    fun setInstallReferer(installReferer: InstallReferer) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.setInstallReferer(installReferer).collect()
        }
    }

    fun getInstallReferer() = trackerUseCase.getInstallReferer()

    fun setService(service: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.setService(service).collect()
        }
    }

    fun getService() = trackerUseCase.getService()

    fun trackOpenApplication(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackApplication(path, "open_app").collect()
        }
    }

    fun trackResumeApplication(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackApplication(path, "resume_app").collect()
        }
    }

    fun trackMinimizeApplication(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackApplication(path, "minimize_app").collect()
        }
    }

    fun trackCloseApplication(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackApplication(path, "close_app").collect()
        }
    }

    fun trackPage(path: String, pageName: String, pageId: String, pageUrlPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackPage(path, pageName, pageId, pageUrlPath).collect()
        }
    }

    fun trackPageDetail(path: String, detail: Any?) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackPageDetail(path, detail).collect()
        }
    }

    fun trackClick(
        path: String,
        name: String,
        category: String? = null,
        url: String? = null,
        details: Any? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                trackerUseCase.trackClick(path, name, category, url, details).collect()
            }
            catch (_: Error) {

            }
        }
    }

    fun trackFilters(path: String, filters: List<TrackerFilterDetail>, category: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackFilters(path, filters, category).collect()
        }
    }

    fun trackSort(path: String, sortType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackSort(path, sortType).collect()
        }
    }

    fun trackSubmission(
        path: String,
        name: String,
        category: String,
        status: Boolean,
        reason: String?,
        details: Any? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackSubmission(path, name, category, status, reason, details).collect()
        }
    }

    fun <S, T> trackImpression(
        path: String,
        category: String,
        time: Long,
        data: List<*>,
        mapper: ((data: S) -> T)? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackImpression<S, T>(path, category, time, data, mapper).collect()
        }
    }

    fun trackDisplayedItems(path: String, data: MutableList<Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackDisplayedItems(path, data).collect()
        }
    }

    fun trackSearch(path: String, keyword: String, details: Any? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerUseCase.trackSearch(path, keyword, details).collect()
        }
    }

    fun getData() = trackerUseCase.getData()

}