package uz.tikoncha_parent.data.remote.model.logout

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val success: Boolean,
    val data: LogoutData? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class LogoutData(
    val items: List<LogoutResponseDto>
)
