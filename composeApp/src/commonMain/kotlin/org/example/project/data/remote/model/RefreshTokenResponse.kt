package org.example.project.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class RefreshTokenResponse(
    val success: Boolean,
    val data: RefreshTokenResponseData?,
    val error: String? = null,
    val code: Int,
)

@Serializable
data class RefreshTokenResponseData(
    val access_token: String = "",
    val access_token_expires_in: Long = 0,
    val token_type: String = ""
)