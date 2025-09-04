package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AvatarResponse(
    val success: Boolean,
    val data: AvatarDto? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class AvatarDto(
    val avatar_url: String
)