package uz.tikoncha_parent.data.remote.telegram

import uz.tikoncha_parent.platform.BuildConfig

object TelegramConfig {
    const val CLIENT_ID: String = "8358248073"

    // Android — debug/release SHA farqli, 2 ta host
    const val ANDROID_HOST_DEBUG: String = "app3389234510-login.tg.dev"
    const val ANDROID_HOST_RELEASE: String = "app3295584582-login.tg.dev"

    // iOS — alohida host(lar)
    const val IOS_HOST: String = "app2596149294-login.tg.dev"

    val SCOPES: List<String> = listOf("openid", "profile", "phone", "telegram:bot_access")

    // Joriy platforma + build uchun host
    val redirectHost: String
        get() = if (BuildConfig.deviceType == "IOS") {
            IOS_HOST
        } else {
            if (BuildConfig.isDebug) ANDROID_HOST_DEBUG else ANDROID_HOST_RELEASE
        }

    // Redirect qaytganda — barcha hostlarga tekshiramiz (har platforma o'zinikiga tushadi)
    fun isTelegramHost(host: String?): Boolean =
        host == ANDROID_HOST_DEBUG || host == ANDROID_HOST_RELEASE ||
                host == IOS_HOST || host == "tglogin"
}