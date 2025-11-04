package id.co.edtslib.tracker.di

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.co.edtslib.tracker.data.TrackerApps
import id.co.edtslib.tracker.data.TrackerConfig
import id.co.edtslib.tracker.data.TrackerData
import id.co.edtslib.tracker.data.TrackerDataList
import java.lang.Exception

class TrackerLocalDataSource(
    sharedPreferences: SharedPreferences,
    app: Application,
    private val config: TrackerConfig
): LocalDataSource<List<TrackerData>>(sharedPreferences) {
    override fun getKeyName(): String = "trackers"
    override fun getValue(json: String): List<TrackerData> = Gson().fromJson(json, object : TypeToken<List<TrackerData>>() {}.type)

    val apps = TrackerApps.create(app.applicationContext, config.appVersion)
    fun add(trackerData: TrackerDataList) {
        if (config.resend) {
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