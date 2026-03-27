package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.domain.model.ChatMessageMetaDto


@Serializable
data class WSRequest<T>(
    val type: String,
    val payload:T? = null,
)

@Serializable
data class WSSendMessage(
    val chat_id: String,
    val type: String,
    val text: String,
    val reply_to_id: String?,
    val client_msg_id: String?,
    val attachment_url: String?,
    val meta: ChatMessageMetaDto?
)

@Serializable
data class WSReadMessage(
    val chat_id: String,
    val message_id: String
)


@Serializable
data class WSResponse<T>(
    val type: String,
    val data:T? = null
)

@Serializable
data class WSMessageCreated(
    val message: ChatMessageDto
)

@Serializable
data class WSMessageUpdated(
    val message: ChatMessageDto
)

@Serializable
data class WSReadUpdate(
    val chat_id: String,
    val reader_id: String,
    val message_id: String,
)

@Serializable
data class WSError(
    val error: String
)

sealed class ChatWsEvent {
    data class MessageCreated(val message: ChatMessageDto): ChatWsEvent()
    data class MessageUpdated(val message: ChatMessageDto): ChatWsEvent()
    data class ReadUpdate(val chatId: String, val readerId: String, val messageId: String): ChatWsEvent()
    data class Error(val error: String): ChatWsEvent()
    object Pong: ChatWsEvent()
    data class Raw(val type: String): ChatWsEvent()
}
