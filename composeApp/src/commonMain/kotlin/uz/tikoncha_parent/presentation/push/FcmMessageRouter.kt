package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.domain.model.PayloadType
import uz.tikoncha_parent.domain.use_case.chat.MapPayloadToDeepLinkUseCase
import uz.tikoncha_parent.domain.use_case.chat.ParseFcmPayloadUseCase

object FcmMessageRouter {

    private val parse = ParseFcmPayloadUseCase()
    private val map   = MapPayloadToDeepLinkUseCase()

    fun handle(rawPayload: String?, fallbackTitle: String?, fallbackBody: String?, uiReady: Boolean) {
        val payload = parse(rawPayload)
        if (payload == null) {
            val link = DeepLink.General(fallbackTitle, fallbackBody)
//            if (uiReady) DeepLinkBus.open(link) else PendingDeepLinks.enqueue(link)
            return
        }
        val link = map(payload, fallbackTitle, fallbackBody) ?: return
//        if (uiReady) DeepLinkBus.open(link) else PendingDeepLinks.enqueue(link)
    }
}