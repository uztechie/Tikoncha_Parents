package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UpsertRuleResponse(
    val success: Boolean,
    val error: String? = null,
    val code: Int? = null,
)