package uz.tikoncha_parent.data.remote.telegram

actual class TelegramAuthClient {
    actual suspend fun login(): TelegramAuthResult =
        TelegramLoginCoordinator.startLogin()
}