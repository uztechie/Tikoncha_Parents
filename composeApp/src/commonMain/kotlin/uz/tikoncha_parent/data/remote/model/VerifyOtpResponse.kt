package uz.saidburxon.newedu.data.model

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.data.remote.model.UserInfoDto

@Serializable
data class VerifyOtpResponse(
    val success: Boolean,
    val data: VerifyOtpResponseData? = null,
    val error: String? = null,
    val code: Int,
)


@Serializable
data class VerifyOtpResponseData(
    val access_token: String? = null,
    val refresh_token: String? = null,
    val access_token_expires_in: Long? = null,
    val refresh_token_expires_in: Long? = null,
    val token_type: String? = null,
    val user_id: String? = null,
    val user_info: UserInfoDto? = null
)

