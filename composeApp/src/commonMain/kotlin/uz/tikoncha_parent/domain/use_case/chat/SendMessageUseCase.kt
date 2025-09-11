package uz.tikoncha_parent.domain.use_case.chat

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChatRepository


class SendMessageUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, text: String, clientMsgId: String? = null): Resource<Boolean> {
        return try {
            repository.sendMessage(chatId, text, clientMsgId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }
    }
}