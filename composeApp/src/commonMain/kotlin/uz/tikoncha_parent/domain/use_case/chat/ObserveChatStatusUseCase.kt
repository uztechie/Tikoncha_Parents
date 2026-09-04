package uz.tikoncha_parent.domain.use_case.chat

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.tikoncha_parent.data.remote.model.ChatMemberDto
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.ChatRepository

class ObserveChatStatusUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(
        chatId: String,
        periodMs: Long = 15_000L
    ): Flow<Outcome<List<ChatMemberDto>>> = flow {
        if (chatId.isBlank()) return@flow

        while (true) {
            emit(repository.chatStatus(chatId))
            delay(periodMs)
        }
    }
}