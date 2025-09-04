package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePolicyRequest(
    val name: String,
    val scope_type: String,
    val scope_id: String,
    val is_active: Boolean
)
