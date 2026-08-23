package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.permission_status.PermissionIssue
import uz.tikoncha_parent.domain.model.permission_status.PermissionStatusType

interface PermissionStatusRepository {
    suspend fun permissionStatus(
        childId: String,
        state: PermissionStatusType,
    ): Outcome<List<PermissionIssue>>
}