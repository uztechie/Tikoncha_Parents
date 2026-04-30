package uz.tikoncha_parent.data.remote.model.permission_status

import kotlinx.serialization.Serializable

@Serializable
data class PermissionStatusRequest(
    val userId: String,
    val state: String
) {
}