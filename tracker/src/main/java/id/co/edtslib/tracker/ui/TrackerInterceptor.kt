package id.co.edtslib.tracker.ui

import android.app.Activity
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import id.co.edtslib.tracker.R
import id.co.edtslib.tracker.Tracker
import kotlin.math.pow
import kotlin.math.sqrt

object TrackerInterceptor {
    fun touchDispatch(activity: Activity) {
        val original = activity.window.callback
        activity.window.callback = object : Window.Callback by original {
            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
                onDispatchTouchEvent(activity, ev)
                return original.dispatchTouchEvent(ev)
            }
        }
    }

    private fun findClickHandle(view: View?) {
        if (view != null) {
            val label = view.getTag(R.id.tracker_click_label) as String?
            val category = view.getTag(R.id.tracker_click_category) as String?
            val textEnabled = view.getTag(R.id.tracker_click_text_enabled) as Boolean?
            val details = view.getTag(R.id.tracker_click_details)

            if (textEnabled == true && view is TextView) {
                Tracker.trackClick(
                    name = view.text.toString(),
                    category = category,
                    details = details
                )

                return

            }

            if (label != null) {
                Tracker.trackClick(
                    name = label.toString(),
                    category = category,
                    details = details
                )

                return
            }

            if (view.parent != null) {
                if (view.parent is ViewGroup) {
                    findClickHandle(view.parent as ViewGroup)
                }
            }
        }
    }

    private var pressedX = 0f
    private var pressedY = 0f

    private fun onDispatchTouchEvent(activity: Activity, ev: MotionEvent?) {
        if (ev?.actionMasked == MotionEvent.ACTION_DOWN) {
            pressedX = ev.x
            pressedY = ev.y
        }
        else
            if (ev?.actionMasked == MotionEvent.ACTION_UP) {
                if (activity.window.decorView is ViewGroup) {

                    val d = pxToDp(activity, distance(pressedX.toDouble(), pressedY.toDouble(), ev.x.toDouble(), ev.y.toDouble()))
                    if (d < 15.0) {
                        val v = findView(
                            ev.x.toDouble(),
                            ev.y.toDouble(),
                            activity.window.decorView as ViewGroup)
                        findClickHandle(v)
                    }
                }
            }
    }

    private fun pxToDp(activity: Activity, px: Double): Double {
        return px / activity.resources.displayMetrics.density
    }

    fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2) * 1.0)
    }

    private fun findView(x: Double, y: Double, view: ViewGroup): View? {
        for (i in 0 until view.childCount) {
            val v = view.getChildAt(i)

            val rect = Rect()
            v.getGlobalVisibleRect(rect)
            if (rect.contains(x.toInt(), y.toInt())) {
                if (v is ViewGroup) {
                    return findView(x, y, v)
                }
                else {
                    return v
                }
            }
        }

        return null
    }
}