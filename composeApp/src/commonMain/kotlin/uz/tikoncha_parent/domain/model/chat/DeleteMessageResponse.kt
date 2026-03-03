package uz.tikoncha_parent.domain.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessageResponse(
    val success: Boolean,
    val error: String?,
    val code:Int
)

