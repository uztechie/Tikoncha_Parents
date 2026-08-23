package uz.tikoncha_parent.data.remote.model.auth


import kotlinx.serialization.Serializable
import uz.tikoncha_parent.data.remote.model.VerifyOtpResponseData

/**
 * Backend access_token + refresh_token + user_info ni xuddi
 * VerifyOtpResponseData kabi qaytaradi.
 */
@Serializable
data class TelegramLoginResponse(
    val success: Boolean,
    val data: VerifyOtpResponseData? = null,
    val error: String? = null,
    val code: Int,
)