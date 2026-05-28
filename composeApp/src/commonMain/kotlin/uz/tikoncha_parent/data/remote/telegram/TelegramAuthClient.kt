package uz.tikoncha_parent.data.remote.telegram

/**
 * Platform-specific Telegram authentication client.
 * Android'da org.telegram:login-sdk ishlatadi,
 * iOS'da Swift bridge orqali TelegramLogin SDK'ga ulanadi.
 */
expect class TelegramAuthClient {
    suspend fun login(): TelegramAuthResult
}