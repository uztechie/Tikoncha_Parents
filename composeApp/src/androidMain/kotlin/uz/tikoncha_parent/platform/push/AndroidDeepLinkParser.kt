package uz.tikoncha_parent.platform.push

import android.content.Intent
import uz.tikoncha_parent.domain.model.DeepLink

object AndroidDeepLinkParser {

    fun parse(intent: Intent?): DeepLink? {
        val uri = intent?.data ?: return null
        if (uri.scheme != DeepLinks.SCHEME) return null

        return when(uri.host){
            DeepLinks.Host.CHAT -> uri.getQueryParameter(DeepLinks.Param.CHAT_ID)?. let {
                DeepLink.Chat(chatId = it, chatTitle = uri.getQueryParameter(DeepLinks.Param.CHAT_TITLE))
            }

            DeepLinks.Host.PARENTAL_REQUEST -> DeepLink.ParentalRequest
            DeepLinks.Host.STRICT_DISABLE -> DeepLink.StrictDisable
            DeepLinks.Host.HOME -> DeepLink.General()
            else -> null
        }
    }
}