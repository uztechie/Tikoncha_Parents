package uz.tikoncha_parent.domain.use_case.chat

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.tikoncha_parent.data.remote.model.ChatMemberDto
import uz.tikoncha_parent.domain.model.Resource

class ObserveChatStatusUseCase (
    private val chatStatusUseCase: ChatStatusUseCase
) {
    operator fun invoke(
        chatId: String,
        periodMs: Long = 15_000L
    ): Flow<Resource<List<ChatMemberDto>>> = flow {
        if (chatId.isBlank()) return@flow

        while (true) {
            emit(chatStatusUseCase(chatId))
            delay(periodMs)
        }
    }
}