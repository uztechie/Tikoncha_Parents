package uz.tikoncha_parent.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openTelegram(phoneNumber: String) {
    val phone = phoneNumber.removePrefix("+")
    val urlString = "https://t.me/tikoncha_bot?start=$phone"
    val telegramDeeplink = NSURL.URLWithString("tg://resolve?domain=tikoncha_bot&start=$phone")
    val browserUrl = NSURL.URLWithString(urlString)
    val app = UIApplication.sharedApplication

    if (telegramDeeplink != null && app.canOpenURL(telegramDeeplink)) {
        app.openURL(
            url = telegramDeeplink,
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    } else if (browserUrl != null) {
        app.openURL(
            url = browserUrl,
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}