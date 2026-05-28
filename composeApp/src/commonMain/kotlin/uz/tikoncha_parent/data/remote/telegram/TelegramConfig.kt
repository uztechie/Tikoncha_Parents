package uz.tikoncha_parent.data.remote.telegram


object TelegramConfig {
    // BotFather'dan olingan Client ID (raqam)
    const val CLIENT_ID: String = "8358248073"

    // BotFather generatsiya qiladi: app{CLIENT_ID}-login.tg.dev
    // Masalan: app123456-login.tg.dev
    const val REDIRECT_HOST: String = "app2429439482-login.tg.dev"

    // Android: /tglogin path bilan
    const val REDIRECT_URI_ANDROID: String = "https://$REDIRECT_HOST/tglogin"

    // iOS: faqat root host
    const val REDIRECT_URI_IOS: String = "https://app2596149294-login.tg.dev"

    // Scope'lar: openid majburiy, profile va phone — sizning talab
    val SCOPES: List<String> = listOf("openid", "profile", "phone")
}