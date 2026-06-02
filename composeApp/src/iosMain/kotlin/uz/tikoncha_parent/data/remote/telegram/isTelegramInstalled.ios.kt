package uz.tikoncha_parent.data.remote.telegram

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun isTelegramAppInstalled(): Boolean {
    val url = NSURL.URLWithString("tg://") ?: return false
    return UIApplication.sharedApplication.canOpenURL(url)
}