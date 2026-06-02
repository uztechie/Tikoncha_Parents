package uz.tikoncha_parent.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.tikoncha_parent.domain.model.auth.TelegramAuthResult

interface TelegramAuthRepository {
    val pendingResult: StateFlow<TelegramAuthResult?>
    fun startLogin(): Boolean
    fun consumePending()
    fun isTelegramInstalled(): Boolean
}