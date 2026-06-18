// commonMain/.../presentation/push/PushCoordinator.kt
package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.domain.use_case.push.ParseFcmPayloadUseCase
import uz.tikoncha_parent.platform.Logger

class PushCoordinator(
    private val parse: ParseFcmPayloadUseCase,
    private val notifier: PushNotifier,
) {
    fun onMessage(payloadRaw: String?) {
        Logger.d("Push", "RAW = $payloadRaw")
        val payload = parse(payloadRaw) ?: run { Logger.d("Push", "parse=NULL"); return }
        val link = payload.toDeepLink()
        Logger.d("Push", "type=${payload.type}  link=$link")   // ⬅️ shu qatorni kuzating
        notifier.show(PushNotification(payload.title, payload.message, link))
    }

    fun onTapped(link: DeepLink?, uiReady: Boolean) {
        Logger.d("Push", "onTapped link=$link uiReady=$uiReady")
        link ?: return
        if (uiReady) DeepLinkBus.open(link) else PendingDeepLinks.enqueue(link)
    }

    fun onTappedRaw(payloadRaw: String?, uiReady: Boolean) {
        Logger.d("Push", "onTappedRaw RAW=$payloadRaw")
        val link = parse(payloadRaw)?.toDeepLink()
        Logger.d("Push", "onTappedRaw link=$link")
        onTapped(link, uiReady)
    }
}