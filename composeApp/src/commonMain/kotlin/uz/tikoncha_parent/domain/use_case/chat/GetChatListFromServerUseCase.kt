package uz.tikoncha_parent.domain.use_case.chat

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.ChatDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChatRepository


class GetChatListFromServerUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(): Resource<List<ChatDto>>{
        return try {
            val response = repository.chatList()
            if (response.success && response.data != null) {
                Resource.Success(response.data)
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