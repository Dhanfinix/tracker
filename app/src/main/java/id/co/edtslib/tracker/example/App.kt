package id.co.edtslib.tracker.example

import android.app.Application
import id.co.edtslib.tracker.Tracker

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        Tracker.debugging = true
        Tracker.appVersion = "1.2.3"
        Tracker.init(
            app = this,
            baseUrl = "https://us-central1-idm-klik-dwh-apollo-dev.cloudfunctions.net/klikidm_apollo_apps_tracker_gateway/",
            token = "AIzaSyCOi2whcq-BY-93oJKmuj5cGLMm9PXyciQ"
        )
//        Tracker.init(
//            baseUrl = PLACEHOLDER_TRACKER_URL,
//            token = "AIzaSyCOi2whcq-BY-93oJKmuj5cGLMm9PXyciQ"
//        )
    }
}