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
    val apps: GetRulesApps
)

@Serializable
data class GetRulesApps(
    val deny: List<GetRulesApp>,
    val allow: List<GetRulesApp>,
)

@Serializable
data class GetRulesApp(
    val id: String,
    val policy_id: String,
    val scope_type: String,
    val value: String,
    val action: String,
)
