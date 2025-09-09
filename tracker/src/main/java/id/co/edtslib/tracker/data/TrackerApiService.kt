package id.co.edtslib.tracker.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TrackerApiService {

    @POST("{path}")
    suspend fun sendTracks(
        @Path("path") path: String,
        @Body track: TrackerDataList,
    ): Response<String>

}