package uz.tikoncha_parent.presentation.push

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.use_case.push.ParseFcmPayloadUseCase

/** Swift (AppDelegate) shu obyektni chaqiradi (eski Kmp*Bridge'lar o'rniga) */
object IosPushEntry : KoinComponent {
    private val coordinator: PushCoordinator by inject()
    private val parse: ParseFcmPayloadUseCase by inject()

    fun submitToken(token: String) {
        AppSettings.fcmToken = token
        FcmTokenRegister.submit(token)
    }

    fun onMessage(payloadRaw: String?) = coordinator.onMessage(payloadRaw)

    fun onTap(payloadRaw: String?) {
        val link = parse(payloadRaw)?.toDeepLink()
        coordinator.onTapped(link, uiReady = false) // iOS tap'da UI odatda tayyor emas
    }
}