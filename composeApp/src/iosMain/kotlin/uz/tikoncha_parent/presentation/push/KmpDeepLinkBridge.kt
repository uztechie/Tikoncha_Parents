package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.use_case.chat.MapPayloadToDeepLinkUseCase
import uz.tikoncha_parent.domain.use_case.chat.ParseFcmPayloadUseCase

    class KmpDeepLinkBridge {
    fun openFromPayload(rawPayload: String?, fallbackTitle: String?, fallbackBody: String?, uiReady: Boolean) {
        val payload = ParseFcmPayloadUseCase()(rawPayload) ?: return
        val link = MapPayloadToDeepLinkUseCase()(payload, fallbackTitle, fallbackBody) ?: return
        if (uiReady){
            DeepLinkBus.open(link)
        }else{
            PendingDeepLinks.enqueue(link)
        }
    }
}