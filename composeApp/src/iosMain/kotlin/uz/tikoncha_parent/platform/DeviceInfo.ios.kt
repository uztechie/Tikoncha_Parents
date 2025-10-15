package uz.tikoncha_parent.platform

import platform.UIKit.UIDevice

actual fun getDeviceInfo(): DeviceInfo {
    val dev = UIDevice.currentDevice
    return DeviceInfo(
        manufacturer = "Apple",
        modelName    = dev.model,
        os           = "IOS",
        osVersion    = dev.systemVersion
    )
}