package id.co.edtslib.tracker.data

class TrackerRemoteDataSource(
    private val trackerApiService: TrackerApiService
) : BaseDataSource() {

    suspend fun send(path: String, trackers: TrackerDataList) =
        getResult { trackerApiService.sendTracks(path, trackers) }

}