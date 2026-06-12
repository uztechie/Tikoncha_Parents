package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.protection.AccountRequestActionResponse
import uz.tikoncha_parent.data.remote.model.protection.ChildRequestsResponse
import uz.tikoncha_parent.data.remote.model.protection.ProtectionStatusResponse
import uz.tikoncha_parent.data.remote.model.protection.RequestActionResponse

interface ProtectionRepository {
    suspend fun protectionStatus(childId: String): ProtectionStatusResponse

    suspend fun strictDisableRequests(
        childId: String?,
        status: String?,
        limit: Int,
        offset: Int,
    ): ChildRequestsResponse

    suspend fun approveStrictDisableRequest(requestId: String): RequestActionResponse

    suspend fun rejectStrictDisableRequest(requestId: String): RequestActionResponse

    suspend fun updateAccountRequestStatus(
        requestId: String,
        status: String,
    ): AccountRequestActionResponse
}