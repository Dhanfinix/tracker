package id.co.edtslib.tracker.ui

import android.view.View
import id.co.edtslib.tracker.R

fun View.setTrackerClickLabel(label: String) {
    setTag(R.id.tracker_click_label, label)
}

fun View.setTrackerClickCategory(category: String?) {
    setTag(R.id.tracker_click_category, category)
}

fun View.setTrackerClickDetails(details: Any?) {
    setTag(R.id.tracker_click_details, details)
}