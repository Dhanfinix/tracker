package id.co.edtslib.tracker.data

import id.co.edtslib.tracker.Tracker

class TrackerRemoteDataSource(
    private val trackerApiService: TrackerApiService,
    private val config: Tracker.TrackerConfig
) : BaseDataSource() {

    suspend fun send(trackers: TrackerDataList) =
        getResult { trackerApiService.sendTracks(config.path, trackers) }

}