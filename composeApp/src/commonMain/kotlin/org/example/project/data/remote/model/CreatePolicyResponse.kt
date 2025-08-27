package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePolicyResponse(
    val success: Boolean,
    val data: CreatePolicyData? = null,
    val error: String? = null,
    val code: Int? = null,
)

@Serializable
data class CreatePolicyData(
    val id: String
)