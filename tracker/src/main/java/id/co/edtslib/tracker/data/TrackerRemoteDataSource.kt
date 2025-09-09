package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker

class TrackerRemoteDataSource(
    private val trackerApiService: TrackerApiService
) : BaseDataSource() {

    suspend fun send(trackers: TrackerDataList) =
        getResult { trackerApiService.sendTracks(Tracker.path, trackers) }

}