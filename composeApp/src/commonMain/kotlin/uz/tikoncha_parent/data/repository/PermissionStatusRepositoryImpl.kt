package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.PermissionStatusApiService
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusResponse
import uz.tikoncha_parent.domain.repository.PermissionStatusRepository

class PermissionStatusRepositoryImpl(val api: PermissionStatusApiService): PermissionStatusRepository {
    override suspend fun permissionStatus(request: PermissionStatusRequest): PermissionStatusResponse {
        return api.permissionStatus(request)
    }
}