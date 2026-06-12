package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.ProtectionApiService
import uz.tikoncha_parent.data.remote.model.protection.AccountRequestActionResponse
import uz.tikoncha_parent.data.remote.model.protection.ChildRequestsResponse
import uz.tikoncha_parent.data.remote.model.protection.ProtectionStatusResponse
import uz.tikoncha_parent.data.remote.model.protection.RequestActionResponse
import uz.tikoncha_parent.domain.repository.ProtectionRepository

class ProtectionRepositoryImpl(
    private val api: ProtectionApiService,
) : ProtectionRepository {

    override suspend fun protectionStatus(childId: String): ProtectionStatusResponse {
        return api.protectionStatus(childId)
    }

    override suspend fun strictDisableRequests(
        childId: String?,
        status: String?,
        limit: Int,
        offset: Int,
    ): ChildRequestsResponse {
        return api.strictDisableRequests(childId, status, limit, offset)
    }

    override suspend fun approveStrictDisableRequest(requestId: String): RequestActionResponse {
        return api.approveStrictDisableRequest(requestId)
    }

    override suspend fun rejectStrictDisableRequest(requestId: String): RequestActionResponse {
        return api.rejectStrictDisableRequest(requestId)
    }

    override suspend fun updateAccountRequestStatus(
        requestId: String,
        status: String,
    ): AccountRequestActionResponse {
        return api.updateAccountRequestStatus(requestId, status)
    }
}