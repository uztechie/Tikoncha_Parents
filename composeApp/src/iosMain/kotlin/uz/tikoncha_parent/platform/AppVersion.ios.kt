package uz.tikoncha_parent.platform

actual fun getAppVersion(): String {
    return ""
//    return NSBundle.mainBundle.infoDictionary
//        ?.get("CFBundleShortVersionString") as? String ?: ""
}