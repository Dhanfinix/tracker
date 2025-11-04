package id.co.edtslib.tracker.example

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.data.TrackerConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerInitModule {
    @Provides
    @Singleton
    fun provideTrackerConfig(): TrackerConfig {
        return TrackerConfig(
            "https://us-central1-idm-klik-dwh-apollo-dev.cloudfunctions.net/klikidm_apollo_apps_tracker_gateway/",
            "AIzaSyCOi2whcq-BY-93oJKmuj5cGLMm9PXyciQ",
            debugging = true,
            appVersion = "1.2.3"
        )
    }
}