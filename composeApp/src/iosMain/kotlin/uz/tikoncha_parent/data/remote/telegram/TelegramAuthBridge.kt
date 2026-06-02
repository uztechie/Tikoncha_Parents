package uz.tikoncha_parent.data.remote.telegram

interface TelegramAuthBridge {
    fun startLogin(callback: TelegramAuthBridgeCallback)
}

interface TelegramAuthBridgeCallback {
    fun onSuccess(idToken: String)
    fun onError(message: String)
    fun onCancel()
}