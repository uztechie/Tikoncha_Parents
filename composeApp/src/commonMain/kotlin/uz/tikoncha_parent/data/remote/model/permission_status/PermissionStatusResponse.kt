package uz.tikoncha_parent.data.remote.model.permission_status

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.data.remote.model.NewsData

@Serializable
data class PermissionStatusResponse(
    val success: Boolean,
    val data: PermissionStatusData? = null,
    val error: String? = null,
    val code: Int? = null
)

@Serializable
data class PermissionStatusData(
    val user_id: String?,
    val last_reported_at: String?,
    val current_mode: String?,
    val issues: List<PermissionStatusIssus>,

    )

@Serializable
data class PermissionStatusIssus(
    val state: String,
    val missing_permissions: List<String>,
    val title: String,
    val body: String,
    val video_url: String?,

)