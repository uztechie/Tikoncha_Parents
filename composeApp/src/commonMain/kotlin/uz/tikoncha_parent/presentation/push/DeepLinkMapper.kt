package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.domain.model.push.FcmPayload
import uz.tikoncha_parent.domain.model.push.PayloadType

/** FcmPayload -> DeepLink (yagona joy; eski MapPayloadToDeepLinkUseCase o'rniga) */
fun FcmPayload.toDeepLink(): DeepLink? = when (type) {
    PayloadType.CHAT -> body?.message?.chatId?.let {
        DeepLink.Chat(chatId = it, chatTitle = body.message.chatTitle, text = body.message.text)
    }
    PayloadType.PARENTAL_REQUEST -> DeepLink.ParentalRequest
    PayloadType.STRICT_DISABLE -> DeepLink.StrictDisable
    PayloadType.NEWS -> DeepLink.General(title, message)   // hozircha general
    PayloadType.GENERAL -> DeepLink.General(title, message)
}