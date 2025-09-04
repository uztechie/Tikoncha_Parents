package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UpsertRuleRequest(
    val target_user_id: String,
    val `package`: String,
    val action: String
)
