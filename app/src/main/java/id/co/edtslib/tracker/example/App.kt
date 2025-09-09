package id.co.edtslib.tracker.example

import android.app.Application
import id.co.edtslib.tracker.Tracker

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        Tracker.appVersion = "1.0.0"
        Tracker.debugging = true
        Tracker.init(
            this,
            "https://us-central1-idm-klik-dwh-apollo-dev.cloudfunctions.net/klikidm_apollo_apps_tracker_gateway/",
            "AIzaSyCOi2whcq-BY-93oJKmuj5cGLMm9PXyciQ"
        )

    }
}