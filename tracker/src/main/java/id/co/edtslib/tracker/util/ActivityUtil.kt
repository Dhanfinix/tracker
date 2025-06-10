package id.co.edtslib.tracker.util

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build


fun Activity.getMetaString(key: String): String? {
    try {
        val activityInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getActivityInfo(componentName, PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
            }
            else {
                packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            }
        val bundle = activityInfo.metaData
        if (bundle == null) return null
        return bundle.getString(key)
    } catch (_: PackageManager.NameNotFoundException) {
        return null
    }
}

fun Activity.getMetaBoolean(key: String): Boolean? {
    try {
        val activityInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getActivityInfo(componentName, PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
            }
            else {
                packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            }
        val bundle = activityInfo.metaData
        if (bundle == null) return null
        return bundle.getBoolean(key)
    } catch (_: PackageManager.NameNotFoundException) {
        return null
    }
}