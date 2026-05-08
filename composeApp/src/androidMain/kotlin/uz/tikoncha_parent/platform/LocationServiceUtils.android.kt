package uz.tikoncha_parent.platform

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import uz.tikoncha_parent.App
import uz.tikoncha_parent.AppHolder

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

actual fun openAppSettings() {
    val ctx: Context = AppHolder.app
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", ctx.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    ctx.startActivity(intent)
}


actual fun openInExternalMaps(lat: Double, lng: Double, label: String) {
    val uri = "geo:$lat,$lng?q=$lat,$lng($label)".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AppHolder.app.startActivity(intent)
}

actual fun shareLocation(lat: Double, lng: Double, name: String) {
    val text = "$name\nhttps://www.google.com/maps?q=$lat,$lng"
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(send, null).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AppHolder.app.startActivity(chooser)
}