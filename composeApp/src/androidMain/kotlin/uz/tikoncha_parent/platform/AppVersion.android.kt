package uz.tikoncha_parent.platform

import uz.tikoncha_parent.AppHolder

actual fun getAppVersion(): String {
    val context = AppHolder.app.applicationContext
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName ?: ""
}