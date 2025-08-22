package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserResponse(
    val success: Boolean,
    val data: UserInfoDto?,
    val error: String? = null,
    val code:Int
)
