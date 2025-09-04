package uz.tikoncha_parent.project

import platform.UIKit.UIDevice
import uz.tikoncha_parent.Platform

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()