package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpRequest(
    val phone: String
)
