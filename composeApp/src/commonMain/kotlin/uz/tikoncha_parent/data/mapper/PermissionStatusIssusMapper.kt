package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusIssus
import uz.tikoncha_parent.domain.model.permission_status.PermissionIssue

fun PermissionStatusIssus.toPermissionIssue(): PermissionIssue = PermissionIssue(
    state = state,
    missingPermissions = missing_permissions,
    title = title,
    body = body,
    videoUrl = video_url,
)