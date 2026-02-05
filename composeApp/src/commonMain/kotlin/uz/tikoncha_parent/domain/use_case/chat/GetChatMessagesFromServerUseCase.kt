package uz.tikoncha_parent.domain.use_case.chat

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.ChatMessageDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChatRepository

class GetChatMessagesFromServerUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Resource<List<ChatMessageDto>> {
        return try {

            val params = HashMap<String, Any>()
            params["chat_id"] = chatId
            params["limit"] = 200


            val response = repository.chatMessages(params)
            if (response.success && response.data != null) {
                Resource.Success(response.data.items)
            } else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.kutilmagan_xatolik_qayta_urining,
                cause = e
            )
        }
    }
}