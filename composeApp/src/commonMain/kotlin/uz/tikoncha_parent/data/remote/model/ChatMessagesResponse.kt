package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

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
    val reply_to_id: String?,
    val client_msg_id: String?,
    val created_at: String?,
    val edited_at: String?,
    val deleted_at: String?,
    val is_read: Boolean? = null,
    val is_mine: Boolean? = null,
)


