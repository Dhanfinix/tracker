package id.co.edtslib.tracker.example

import android.app.Application
import id.co.edtslib.tracker.Tracker

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        Tracker.Builder(this)
            .setBaseUrl("https://us-central1-idm-klik-dwh-apollo-dev.cloudfunctions.net/klikidm_apollo_apps_tracker_gateway/")
            .setToken("AIzaSyCOi2whcq-BY-93oJKmuj5cGLMm9PXyciQ")
            .setDebugging(true)
            .setAppVersion("1.2.3")
            .build()
    }
}