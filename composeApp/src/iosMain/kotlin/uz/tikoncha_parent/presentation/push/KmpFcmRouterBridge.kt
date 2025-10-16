package uz.tikoncha_parent.presentation.push

class KmpFcmRouterBridge {
    fun handle(rawPayload: String?, fallbackTitle: String?, fallbackBody: String?, uiReady: Boolean) {
        FcmMessageRouter.handle(rawPayload, fallbackTitle, fallbackBody, uiReady)
    }
}