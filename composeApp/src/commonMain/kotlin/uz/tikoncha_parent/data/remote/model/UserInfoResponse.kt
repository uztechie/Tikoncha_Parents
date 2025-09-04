package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val success: Boolean,
    val data: UserInfoDto?,
    val error: String? = null,
    val code:Int
)
