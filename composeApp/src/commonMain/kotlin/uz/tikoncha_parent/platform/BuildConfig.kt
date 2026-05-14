package uz.tikoncha_parent.platform

expect object BuildConfig {
    val isDebug: Boolean
    val deviceType: String // "ANDROID" yoki "IOS"
}