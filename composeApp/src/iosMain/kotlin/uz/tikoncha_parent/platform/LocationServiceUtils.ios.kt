package uz.tikoncha_parent.platform

import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

actual fun isLocationServiceEnabled(): Boolean = CLLocationManager.locationServicesEnabled()

actual fun openLocationSettings() {
    val url = NSURL(string = UIApplicationOpenSettingsURLString)
    UIApplication.sharedApplication.openURL(url)
}