package id.co.edtslib.tracker.example

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import id.co.edtslib.tracker.Tracker

@HiltAndroidApp
class App: Application(){
    override fun onCreate() {
        super.onCreate()
        Tracker.init(
            baseUrl = "https://us-central1-idm-klik-dwh-apollo-dev.cloudfunctions.net/klikidm_apollo_apps_tracker_gateway/",
            token = "AIzaSyCOi2whcq-BY-93oJKmuj5cGLMm9PXyciQ",
            debugging = true,
            appVersion = "1.2.3"
        )
    }
}