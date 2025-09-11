package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatUnreadCountResponse(
    val success: Boolean,
    val data: ChatUnreadData?,
    val error: String?,
    val code:Int

)

@Serializable
data class ChatUnreadData(
   val items: List<ChatUnreadItem>
)


@Serializable
data class ChatUnreadItem(
    val chat_id: String,
    val count: Int
)
