package uz.tikoncha_parent.data.remote.device

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val success: Boolean,
    val error: String?
)
