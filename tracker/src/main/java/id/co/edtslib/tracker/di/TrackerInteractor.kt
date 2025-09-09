package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerFilterDetail

class TrackerInteractor(private val repository: ITrackerRepository) : TrackerUseCase {
    override fun createSession() = repository.createSession()
    override fun setUserId(userId: Long) = repository.setUserId(userId)
    override fun setLatLng(lat: Double?, lng: Double?) = repository.setLatLng(lat, lng)
    override fun setInstallReferer(installReferer: InstallReferer) =
        repository.setInstallReferer(installReferer)

    override fun getInstallReferer() =
        repository.getInstallReferer()

    override fun setService(service: String) = repository.setService(service)
    override fun getService() = repository.getService()

    override fun trackApplication(path: String, eventName: String) = repository.trackApplication(path, eventName)

    override fun trackPage(path: String, pageName: String, pageId: String, pageUrlPath: String) =
        repository.trackPage(path, pageName, pageId, pageUrlPath)

    override fun trackPageDetail(path: String, detail: Any?) = repository.trackPageDetail(path, detail)

    override fun trackClick(path: String, name: String, category: String?, url: String?, details: Any?) =
        repository.trackClick(path, name, category, url, details)

    override fun trackFilters(path: String, filters: List<TrackerFilterDetail>, category: String) =
        repository.trackFilters(path, filters, category)

    override fun trackSort(path: String, sortType: String) = repository.trackSort(path, sortType)

    override fun <S, T> trackImpression(
        path: String,
        category: String,
        time: Long,
        data: List<*>,
        mapper: ((data: S) -> T)?
    ) = repository.trackImpression<S, T>(path, category, time, data, mapper)

    override fun trackSubmission(
        path: String,
        name: String,
        category: String,
        status: Boolean,
        reason: String?,
        details: Any?
    ) =
        repository.trackSubmission(path, name, category, status, reason, details)

    override fun trackDisplayedItems(path: String, data: MutableList<Any>) = repository.trackDisplayedItems(path, data)
    override fun trackSearch(path: String, keyword: String, details: Any?) =
        repository.trackSearch(path, keyword, details)

    override fun getData() = repository.getData()
}