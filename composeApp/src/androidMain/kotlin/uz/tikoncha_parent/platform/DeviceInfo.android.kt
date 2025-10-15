package uz.tikoncha_parent.platform

import android.os.Build

actual fun getDeviceInfo(): DeviceInfo = DeviceInfo(
    manufacturer = Build.MANUFACTURER ?: "ANDROID",
    modelName    = Build.MODEL ?: "ANDROID",
    os          = "ANDROID",
    osVersion   = Build.VERSION.RELEASE ?: Build.VERSION.SDK_INT.toString()
)