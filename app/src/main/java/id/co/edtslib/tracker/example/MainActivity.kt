package id.co.edtslib.tracker.example

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import id.co.edtslib.edtsds.list.menu.MenuDelegate
import id.co.edtslib.edtsds.list.menu.MenuListView
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.data.TrackerFilterDetail
import id.co.edtslib.tracker.ui.TrackerInflateFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Tracker.checkInstallReferrer(this)

        val filter = mutableListOf<TrackerFilterDetail>()
        val tracker = TrackerFilterDetail(
            "Harga",
            "Button",
            listOf("adasd", "gdfgdfgf")
        )
        filter.add(tracker)
        Tracker.trackFilters(filter, "test")

        val list = mutableListOf<String>()
        for (i in 0 until 100) {
            list.add("abah $i")
        }


        val menuListView = findViewById<MenuListView<String>>(R.id.menuListView)
        menuListView.data = list
        menuListView.delegate = object : MenuDelegate<String> {
            override fun onSelected(t: String) {
                finish()
            }
        }

    }



}