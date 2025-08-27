package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRuleResponse(
    val success: Boolean,
    val error: String? = null,
    val code: Int? = null,
)