package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusResponse

interface PermissionStatusRepository {
    suspend fun permissionStatus(request: PermissionStatusRequest): PermissionStatusResponse
}