package org.example.project.platform

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import org.example.project.AppHolder

actual fun isLocationServiceEnabled(): Boolean {
    val context: Context = AppHolder.app
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

actual fun openLocationSettings() {
    val ctx: Context = AppHolder.app
    ctx.startActivity(
        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}