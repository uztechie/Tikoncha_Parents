package uz.tikoncha_parent.data.remote.telegram

sealed interface TelegramAuthResult {
    data class Success(val idToken: String) : TelegramAuthResult
    data class Error(val message: String) : TelegramAuthResult
    data object Cancelled : TelegramAuthResult
}