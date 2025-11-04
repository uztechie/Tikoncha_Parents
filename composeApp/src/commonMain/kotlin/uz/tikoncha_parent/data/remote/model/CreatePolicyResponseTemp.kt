package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePolicyResponseTemp(
    val success: Boolean,
    val data: CreatePolicyDataTemp? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class CreatePolicyDataTemp(
    val id: String
)