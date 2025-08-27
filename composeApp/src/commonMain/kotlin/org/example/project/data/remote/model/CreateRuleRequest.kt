package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRuleRequest(
    val policyId: String,
    val resource_type: String,
    val matcher: String,
    val value: String,
    val action: String,
)
