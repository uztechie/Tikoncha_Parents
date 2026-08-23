package uz.tikoncha_parent.domain.model.permission_status

data class PermissionIssue(
    val state: String,
    val missingPermissions: List<String>,
    val title: String,
    val body: String,
    val videoUrl: String?,
)