package uz.tikoncha_parent.data.remote.telegram

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.tikoncha_parent.domain.model.auth.TelegramAuthResult

object TelegramAuthBus {

    private val _pendingResult = MutableStateFlow<TelegramAuthResult?>(null)
    val pendingResult: StateFlow<TelegramAuthResult?> = _pendingResult.asStateFlow()

    // Platformalar (Android/iOS) SDK natijasini shu yerga emit qiladi
    fun emitResult(result: TelegramAuthResult) { _pendingResult.value = result }
    fun clearPending() { _pendingResult.value = null }
    fun reset() { _pendingResult.value = null }
}