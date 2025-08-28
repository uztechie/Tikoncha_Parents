package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GetRulesResponse(
    val success: Boolean,
    val data: GetRulesData?,
    val error: String? = null,
    val code:Int
)

@Serializable
data class GetRulesData(
    val items: List<GetRuleItem>
)


@Serializable
data class GetRuleItem(
    val `package`: String,
    val decision: String,
)

