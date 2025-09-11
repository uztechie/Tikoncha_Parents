package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatStatusResponse(
    val success: Boolean,
    val data: ChatStatusData?,
    val error: String?,
    val code:Int

)

@Serializable
data class ChatStatusData(
   val members: List<ChatMemberDto>
)


@Serializable
data class ChatMemberDto(
    val user_id: String,
    val first_name: String,
    val last_name: String,
    val avatar: String?,
    val online: Boolean?,
    val last_seen: String?,
)
