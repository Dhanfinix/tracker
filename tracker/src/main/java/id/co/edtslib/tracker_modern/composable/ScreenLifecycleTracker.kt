package id.co.edtslib.tracker_modern.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import id.co.edtslib.tracker_modern.Tracker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

/**
 * This composable is used to mimic page tracker on base activity in EdtsKu.
 * If you use single activity pattern, use this composable to track screen state
 * just like activity.
 */
@Composable
fun ScreenLifecycleTracker(
    pageName: String,
    pageId: String = rememberSaveable { "${pageName}_${Date().time}" }
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    // Local state to track minimizing behavior
    val state = remember {
        object {
            var isMinimizing = false
            var isMinimized = false
        }
    }

    // 1. Track Page View (One time on enter)
    LaunchedEffect(Unit) {
        Tracker.trackPage(pageName, pageId)
        Log.d("ScreenLifecycleTracker", "Page View Tracked: $pageName")
    }

    // 2. Lifecycle Observer
    // We use DisposableEffect to attach a raw observer so we can access the 'event' parameter
    // and remove it when changing screen
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (state.isMinimized) {
                        Tracker.trackResumeApplication()
                        Log.d("ScreenLifecycleTracker", "Resume App Tracked: $pageName")
                    }
                    state.isMinimized = false
                    state.isMinimizing = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    Tracker.resumePage(pageName, pageId)
                    Log.d("ScreenLifecycleTracker", "Resume: $pageName")
                }
                Lifecycle.Event.ON_PAUSE -> {
                    state.isMinimizing = true
                }
                Lifecycle.Event.ON_STOP -> {
                    // Logic: Handle App Minimize with Debounce
                    scope.launch {
                        delay(2500)
                        if (state.isMinimizing) {
                            Tracker.trackMinimizeApplication()
                            Log.d("ScreenLifecycleTracker", "Minimize App Tracked: $pageName")
                            state.isMinimized = true
                        }
                    }
                }
                else -> { /* Ignore Create/Destroy */ }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}