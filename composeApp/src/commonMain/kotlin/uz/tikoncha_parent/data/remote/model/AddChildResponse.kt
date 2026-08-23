package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AddChildResponse(
    val success: Boolean,
    val data: AddChildData?,
    val error: String? = null,
    val code:Int
)

@Serializable
data class AddChildData(
    val code: String,
    val expires_at: String? = null,
)
