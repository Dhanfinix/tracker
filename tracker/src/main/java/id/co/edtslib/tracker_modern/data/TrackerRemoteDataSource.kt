package id.co.edtslib.tracker_modern.data


class TrackerRemoteDataSource(
    private val trackerApiService: TrackerApiService,
    private val config: TrackerConfig
) : BaseDataSource() {

    suspend fun send(trackers: TrackerDataList) =
        getResult { trackerApiService.sendTracks(config.path, trackers) }

}