package uz.tikoncha_parent.data.repository

import kotlinx.coroutines.flow.StateFlow
import uz.tikoncha_parent.data.remote.telegram.TelegramAuthBus
import uz.tikoncha_parent.data.remote.telegram.isTelegramAppInstalled
import uz.tikoncha_parent.data.remote.telegram.startTelegramLogin
import uz.tikoncha_parent.domain.model.auth.TelegramAuthResult
import uz.tikoncha_parent.domain.repository.TelegramAuthRepository

class TelegramAuthRepositoryImpl : TelegramAuthRepository {
    override val pendingResult: StateFlow<TelegramAuthResult?>
        get() = TelegramAuthBus.pendingResult

    override fun startLogin(): Boolean {
        TelegramAuthBus.reset()
        return startTelegramLogin()
    }

    override fun consumePending() = TelegramAuthBus.clearPending()

    override fun isTelegramInstalled(): Boolean = isTelegramAppInstalled()
}