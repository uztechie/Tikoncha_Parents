package uz.tikoncha_parent.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

actual fun isLocationServiceEnabled(): Boolean = CLLocationManager.locationServicesEnabled()

actual fun openLocationSettings() {
    val url = NSURL(string = UIApplicationOpenSettingsURLString)
    UIApplication.sharedApplication.openURL(url)
}

actual fun openAppSettings() {
    val url = NSURL(string = UIApplicationOpenSettingsURLString)
    UIApplication.sharedApplication.openURL(url)
}

@OptIn(ExperimentalForeignApi::class)
actual fun openInExternalMaps(lat: Double, lng: Double, label: String) {
    val encoded = label.replace(" ", "%20")
    val url = NSURL(string = "http://maps.apple.com/?ll=$lat,$lng&q=$encoded")
    UIApplication.sharedApplication.openURL(url)
}

@OptIn(ExperimentalForeignApi::class)
actual fun shareLocation(lat: Double, lng: Double, name: String) {
    val text = "$name\nhttps://maps.apple.com/?ll=$lat,$lng"
    val vc = UIActivityViewController(activityItems = listOf(text), applicationActivities = null)
    UIApplication.sharedApplication.keyWindow?.rootViewController
        ?.presentViewController(vc, animated = true, completion = null)
}