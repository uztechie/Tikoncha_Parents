package uz.tikoncha_parent.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual fun hideKeyboardPlatform() {
    UIApplication.sharedApplication.sendAction(
        NSSelectorFromString("resignFirstResponder"),
        null,null,null
    )
}