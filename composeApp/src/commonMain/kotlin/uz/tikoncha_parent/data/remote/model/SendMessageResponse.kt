package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponse(
    val success: Boolean,
    val error: String? = null,
    val code: Int? = null,

)
