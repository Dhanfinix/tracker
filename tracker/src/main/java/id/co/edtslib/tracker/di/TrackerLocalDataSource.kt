package id.co.edtslib.tracker.di

import android.app.Application
import android.content.SharedPreferences
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.data.TrackerApps
import id.co.edtslib.tracker.data.TrackerData
import id.co.edtslib.tracker.data.TrackerDataList
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.lang.Exception

class TrackerLocalDataSource(
    sharedPreferences: SharedPreferences,
    app: Application,
): LocalDataSource<List<TrackerData>>(sharedPreferences) {
    override fun getKeyName(): String = "trackers"
    override fun getValue(json: String): List<TrackerData> =
        Json.decodeFromString(ListSerializer(TrackerData.serializer()), json)

    val apps = TrackerApps.create(app.applicationContext, Tracker.appVersion)
    fun add(trackerData: TrackerDataList) {
        if (Tracker.resend) {
            try {
                val cached = getCached()
                val list = cached?.toMutableList() ?: mutableListOf()
                list.addAll(trackerData.data)

                save(list)
            } catch (e: Exception) {
                // nothing to do
            }
        }
    }
}