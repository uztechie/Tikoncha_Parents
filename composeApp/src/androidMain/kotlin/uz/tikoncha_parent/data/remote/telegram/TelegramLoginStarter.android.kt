package uz.tikoncha_parent.data.remote.telegram

actual fun startTelegramLogin(): Boolean = TelegramLoginCoordinator.startLogin()