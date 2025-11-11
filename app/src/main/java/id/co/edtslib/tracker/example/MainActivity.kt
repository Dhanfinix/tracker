package id.co.edtslib.tracker.example

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.edtslib.edtsds.list.menu.MenuListView
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.data.TrackerFilterDetail
import id.co.edtslib.tracker.util.toSafeJsonElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject

@Serializable
data class UserData(
    val username: String,
    val age: Int,
    val address: String?,
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tracker: Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = UserData("Ramdhan Muhammad", 25, null)
        val dataMap = mutableMapOf<String, Any?>()
        dataMap["username"] = data.username
        dataMap["age"] = data.age
        dataMap["address"] = data.address
        tracker.trackClick("aaa", details = dataMap)
        tracker.checkInstallReferrer(this)

        val filter = mutableListOf<TrackerFilterDetail>()
        val trackerData = TrackerFilterDetail(
            "Harga",
            "Button",
            listOf("adasd", "gdfgdfgf").toSafeJsonElement()
        )
        filter.add(trackerData)
        tracker.trackFilters(filter, "test")

        val list = mutableListOf<UserData>()
        for (i in 0 until 100) {
            list.add(UserData(
                username = "Ramdhan Muhammad $i",
                age = 25,
                address = null
            ))
        }


        val menuListView = findViewById<MenuListView<UserData>>(R.id.menuListView)
        menuListView.data = list
        tracker.setImpressionRecyclerView(
            "abah test",
            menuListView
        ) { imp ->
            try {
                // Check if JsonNull
                if (imp is JsonNull) {
                    return@setImpressionRecyclerView null
                }

                val source = Json.decodeFromJsonElement(UserData.serializer(), imp)
                val mapped = String.format(
                    "%s manipulated",
                    source.username
                )
                return@setImpressionRecyclerView JsonPrimitive(mapped)
            } catch (e: Exception) {
                Log.e("Tracker", "Mapper error: ${e.message}")
                null
            }
        }

        tracker.trackImpression("", list.toSafeJsonElement())

    }

    override fun onResume() {
        super.onResume()
        //Tracker.resumePage("testlib8", "testlibaja8")
        tracker.trackPage("testlib11", "testlib11", "testlib11")
        Handler(Looper.myLooper()!!).postDelayed({
            tracker.trackSearch("lalali", "Login")
        }, 3000)
    }
}