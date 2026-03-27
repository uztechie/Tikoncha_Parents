package uz.tikoncha_parent.domain.use_case.chat

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.WSSendMessage
import uz.tikoncha_parent.domain.model.ChatMessageMetaDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.ChatRepository
import uz.tikoncha_parent.presentation.model.ChatMessageType


class SendMessageUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        text: String,
        type: ChatMessageType,
        replyToId: String? = null,
        clientMsgId: String? = null,
        meta: ChatMessageMetaDto? = null
    ): Resource<Boolean> {
        return try {
            val message = WSSendMessage(
                chat_id = chatId,
                type = type.name,
                text = text,
                reply_to_id = replyToId,
                client_msg_id = clientMsgId,
                attachment_url = null,
                meta = meta
            )
            repository.sendMessage(message)
            Resource.Success(true)
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