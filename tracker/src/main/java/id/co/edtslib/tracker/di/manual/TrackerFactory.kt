package id.co.edtslib.tracker.di.manual

import id.co.edtslib.tracker.Tracker

object TrackerFactory {
    private var tracker: Tracker? = null
    fun getTracker() = tracker ?: Tracker().also { tracker = it  }
}