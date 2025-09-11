package uz.tikoncha_parent.domain.use_case.chat

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.daq
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.ChatMemberDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChatRepository


class ChatStatusUseCase (
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Resource<List<ChatMemberDto>> {
        return try {
            val response = repository.chatStatus(chatId)
            if (response.success && response.data != null) {
                Resource.Success(response.data.members)
            } else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }
    }
}