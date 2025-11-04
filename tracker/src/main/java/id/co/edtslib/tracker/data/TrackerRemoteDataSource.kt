package id.co.edtslib.tracker.data

class TrackerRemoteDataSource(
    private val trackerApiService: TrackerApiService,
    private val config: TrackerConfig
) : BaseDataSource() {

    suspend fun send(trackers: TrackerDataList) =
        getResult { trackerApiService.sendTracks(config.path, trackers) }

}