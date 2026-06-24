package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UnlinkChildResponse(
    val success: Boolean = false,
    val data: UnlinkChildData? = null,
    val error: String? = null,
    val code: Int? = null
)

@Serializable
data class UnlinkChildData(
    val message: String? = null
)