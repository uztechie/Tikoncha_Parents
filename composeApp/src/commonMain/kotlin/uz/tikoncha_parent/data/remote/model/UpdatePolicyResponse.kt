package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePolicyResponse(
    val success: Boolean,
    val data: CreatePolicyData? = null,
    val error: String? = null,
    val code: Int? = null,
)



