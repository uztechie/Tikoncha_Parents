package uz.tikoncha_parent.domain.use_case.chat

import uz.tikoncha_parent.domain.repository.ChatRepository

class DisconnectChatWebSocketUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke() = repository.disconnect()
}