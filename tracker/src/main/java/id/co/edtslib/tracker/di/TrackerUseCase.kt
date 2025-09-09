package id.co.edtslib.tracker.di

import id.co.edtslib.tracker.data.InstallReferer
import id.co.edtslib.tracker.data.TrackerFilterDetail
import id.co.edtslib.tracker.data.TrackerData
import id.co.edtslib.tracker.data.TrackerResponse
import kotlinx.coroutines.flow.Flow

interface TrackerUseCase {
    fun createSession(): Flow<String?>
    fun setUserId(userId: Long): Flow<Long>
    fun setLatLng(lat: Double?, lng: Double?): Flow<Boolean>
    fun setInstallReferer(installReferer: InstallReferer): Flow<Boolean>
    fun getInstallReferer(): InstallReferer?
    fun setService(service: String): Flow<Boolean>
    fun getService(): String?

    fun trackApplication(path: String, eventName: String): Flow<Boolean>

    fun trackPage(path: String, pageName: String, pageId: String, pageUrlPath: String): Flow<TrackerResponse>
    fun trackPageDetail(path: String, detail: Any?): Flow<TrackerResponse>

    fun trackClick(path: String, name: String, category: String? = null, url: String? = null, details: Any? = null): Flow<TrackerResponse>
    fun trackFilters(path: String, filters: List<TrackerFilterDetail>, category: String = ""): Flow<TrackerResponse>
    fun trackSort(path: String, sortType: String): Flow<TrackerResponse>

    fun <S, T> trackImpression(path: String, category: String, time: Long, data: List<*>, mapper: ((data: S) -> T)? = null): Flow<TrackerResponse>
    fun trackSubmission(path: String, name: String, category: String, status: Boolean, reason: String?, details: Any? = null): Flow<TrackerResponse>

    fun trackDisplayedItems(path: String, data: MutableList<Any>): Flow<TrackerResponse>
    fun trackSearch(path: String, keyword: String, details: Any? = null): Flow<TrackerResponse>

    fun getData(): TrackerData
}