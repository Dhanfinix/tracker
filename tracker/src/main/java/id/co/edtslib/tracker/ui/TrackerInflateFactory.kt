package id.co.edtslib.tracker.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import id.co.edtslib.tracker.R

class TrackerInflateFactory(
    private val delegate: LayoutInflater.Factory2?
) : LayoutInflater.Factory2 {
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val view = delegate?.onCreateView(parent, name, context, attrs)
            ?: createViewManually(name, context, attrs)
        if (view != null) {
            attachTrackerAttributes(view, context, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet) =
        onCreateView(null, name, context, attrs)

    private fun attachTrackerAttributes(view: View, context: Context, attrs: AttributeSet) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.obtainStyledAttributes(attrs, R.styleable.TrackerAttrs).use {
                val label = it.getString(R.styleable.TrackerAttrs_trackerClickLabel)
                if (!label.isNullOrEmpty()) {
                    view.setTag(R.id.tracker_click_label, label)
                }

                val category = it.getString(R.styleable.TrackerAttrs_trackerClickCategory)
                if (!category.isNullOrEmpty()) {
                    view.setTag(R.id.tracker_click_category, category)
                }

                val textEnabled = it.getBoolean(R.styleable.TrackerAttrs_trackerClickTextEnabled, false)
                view.setTag(R.id.tracker_click_text_enabled, textEnabled)
            }
        }
    }

    private fun createViewManually(name: String, context: Context, attrs: AttributeSet): View? {
        return try {
            val inflater = LayoutInflater.from(context)
            inflater.createView(name, null, attrs)
        } catch (_: Exception) {
            null
        }
    }
}