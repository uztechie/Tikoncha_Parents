package uz.tikoncha_parent.presentation.policy.app_site_selection

data class SiteUi(
    val url: String,
    val isDefault: Boolean = false,
)

val DEFAULT_SITES = listOf(
    SiteUi(url = "youtube.com", isDefault = true),
    SiteUi(url = "instagram.com", isDefault = true),
    SiteUi(url = "tiktok.com", isDefault = true),
    SiteUi(url = "facebook.com", isDefault = true),
    SiteUi(url = "twitter.com", isDefault = true),
    SiteUi(url = "telegram.org", isDefault = true),
    SiteUi(url = "snapchat.com", isDefault = true),
    SiteUi(url = "whatsapp.com", isDefault = true),
)