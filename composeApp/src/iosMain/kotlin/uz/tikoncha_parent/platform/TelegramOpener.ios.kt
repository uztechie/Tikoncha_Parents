package uz.tikoncha_parent.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

private const val TG_BOT = "tikoncha_bot"
actual fun openTelegram(phoneNumber: String): Boolean {
    val phone = phoneNumber.filter { it.isDigit() }
    if (phone.isEmpty()) return false

    val app = UIApplication.sharedApplication

    // 1. tg:// deep link — Info.plist'da LSApplicationQueriesSchemes'ga
    //    "tg" qo'shilgan bo'lsa ishlaydi. Bo'lmasa canOpenURL false qaytaradi → fallback.
    NSURL.URLWithString("tg://resolve?domain=$TG_BOT&start=$phone")?.let { url ->
        if (app.canOpenURL(url)) {
            app.openURL(url, emptyMap<Any?, Any>(), null)
            return true
        }
    }

    // 2. https://t.me/ — Telegram universal link yoki Safari.
    //    canOpenURL tekshirmaymiz — Safari doimo mavjud.
    NSURL.URLWithString("https://t.me/$TG_BOT?start=$phone")?.let { url ->
        app.openURL(url, emptyMap<Any?, Any>(), null)
        return true
    }

    // 3. App Store — oxirgi fallback
    NSURL.URLWithString("https://apps.apple.com/app/telegram-messenger/id686449807")?.let { url ->
        app.openURL(url, emptyMap<Any?, Any>(), null)
        return true
    }
    return false
}

actual fun openUrl(url: String): Boolean {
    if (url.isBlank()) return false
    val nsUrl = NSURL.URLWithString(url) ?: return false
    UIApplication.sharedApplication.openURL(nsUrl, emptyMap<Any?, Any?>(), null)
    return true
}