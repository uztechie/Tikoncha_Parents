package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.domain.model.ChatMessageMetaDto

@Serializable
data class ChatMessagesResponse(
    val success: Boolean,
    val data: ChatMessagesData? = null,
    val error: String? = null,
    val code: Int? = null,

)

@Serializable
data class ChatMessagesData(
    val items: List<ChatMessageDto>,
    val has_more: Boolean,
)

@Serializable
data class ChatMessageDto(
    val id: String,
    val chat_id: String,
    val sender_id: String,
    val sender_name: String,
    val sender_avatar: String?,
    val type: String,
    val text: String,
    val meta: ChatMessageMetaDto?,
    val reply_to_id: String?,
    val replied_message_owner: String?,
    val replied_message_text: String?,
    val client_msg_id: String?,
    val created_at: String?,
    val edited_at: String?,
    val deleted_at: String?,
    val is_read: Boolean? = null,
    val is_mine: Boolean? = null,
    val attachment_url: String? = null
)


