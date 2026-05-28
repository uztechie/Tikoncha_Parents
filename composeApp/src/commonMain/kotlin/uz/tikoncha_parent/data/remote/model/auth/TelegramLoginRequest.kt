package uz.tikoncha_parent.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class TelegramLoginRequest(
    val id_token: String,
)