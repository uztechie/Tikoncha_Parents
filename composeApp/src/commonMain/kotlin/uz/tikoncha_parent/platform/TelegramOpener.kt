package uz.tikoncha_parent.platform

expect fun openTelegram(phoneNumber: String): Boolean

expect fun openUrl(url: String): Boolean

expect fun shareText(text: String)