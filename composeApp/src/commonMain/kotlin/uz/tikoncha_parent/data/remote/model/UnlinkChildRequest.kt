package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UnlinkChildRequest(
    val child_user_id: String,
    val parent_user_id: String
)