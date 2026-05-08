package uz.tikoncha_parent.platform

expect fun isLocationServiceEnabled(): Boolean
expect fun openLocationSettings()

expect fun openAppSettings()

expect fun openInExternalMaps(lat: Double, lng: Double, label: String)
expect fun shareLocation(lat: Double, lng: Double, name: String)