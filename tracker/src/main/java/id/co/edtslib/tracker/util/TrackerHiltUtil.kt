package id.co.edtslib.tracker.util

import android.app.Application
import dagger.hilt.android.EntryPointAccessors
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.di.ConfigurationLocalSource
import id.co.edtslib.tracker.di.hilt.ConfigurationEntryPoint
import id.co.edtslib.tracker.di.hilt.TrackerEntryPoint

/**
 * Utility object for manually accessing Hilt-provided dependencies from the SingletonComponent
 * using the application context. This is especially useful in classes that are not directly
 * managed by Hilt (e.g., custom views, adapters, or library components).
 *
 * Usage:
 * ```
 * val tracker = TrackerHiltUtil.getTracker(context.applicationContext as Application)
 * val configSource = TrackerHiltUtil.getTrackerConfigLocalSource(context.applicationContext as Application)
 * ```
 *
 * Requirements:
 * - The application class must be annotated with `@HiltAndroidApp`.
 */
object TrackerHiltUtil {

    /**
     * Retrieves the Hilt-injected [Tracker] instance from the application-level SingletonComponent.
     *
     * @param app The application context used to access the Hilt component graph.
     * @return The injected [Tracker] instance.
     */
    fun getTracker(app: Application): Tracker {
        return EntryPointAccessors.fromApplication(
            app.applicationContext,
            TrackerEntryPoint::class.java
        ).tracker()
    }

    /**
     * Retrieves the Hilt-injected [ConfigurationLocalSource] instance from the application-level SingletonComponent.
     *
     * @param app The application context used to access the Hilt component graph.
     * @return The injected [ConfigurationLocalSource] instance.
     */
    fun getTrackerConfigLocalSource(app: Application): ConfigurationLocalSource {
        return EntryPointAccessors.fromApplication(
            app.applicationContext,
            ConfigurationEntryPoint::class.java
        ).configuration()
    }
}