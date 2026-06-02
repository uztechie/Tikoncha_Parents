package uz.tikoncha_parent.data.remote.telegram

import uz.tikoncha_parent.domain.model.auth.TelegramAuthResult

actual fun startTelegramLogin(): Boolean {
    val bridge = TelegramAuthBridgeProvider.bridge ?: return false
    bridge.startLogin(object : TelegramAuthBridgeCallback {
        override fun onSuccess(idToken: String) {
            TelegramAuthBus.emitResult(TelegramAuthResult.Success(idToken))
        }
        override fun onError(message: String) {
            TelegramAuthBus.emitResult(TelegramAuthResult.Error(message))
        }
        override fun onCancel() {
            TelegramAuthBus.emitResult(TelegramAuthResult.Cancelled)
        }
    })
    return true
}