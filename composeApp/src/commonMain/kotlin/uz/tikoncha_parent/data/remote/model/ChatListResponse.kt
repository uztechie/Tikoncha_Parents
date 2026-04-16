package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatListResponse(
    val success: Boolean,
    val data: ChatListData? = null,
    val error: String? = null,
    val code: Int? = null,

)


@Serializable
data class ChatListData(
    val chats: List<ChatDto>,
    val notification: Boolean
)

@Serializable
data class ChatDto(
    val id: String,
    val type: String,
    val title: String,
    val avatar: String? = null,
    val last_message: ChatLastMessage?,
    val unread_count: Int,

)

@Serializable
data class ChatLastMessage(
    val message_id: String,
    val sender_id: String,
    val type: String,
    val text: String,
    val created_at: String,
    val is_read: Boolean,
    val is_mine: Boolean,
)


