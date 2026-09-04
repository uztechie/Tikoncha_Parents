package uz.tikoncha_parent.domain.use_case.chat

import uz.tikoncha_parent.data.remote.model.WSSendMessage
import uz.tikoncha_parent.domain.model.ChatMessageMetaDto
import uz.tikoncha_parent.domain.model.app_error.Outcome
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
    ): Outcome<Unit> = repository.sendMessage(
        WSSendMessage(
            chat_id = chatId,
            type = type.name,
            text = text,
            reply_to_id = replyToId,
            client_msg_id = clientMsgId,
            attachment_url = null,
            meta = meta
        )
    )
}