package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.protection.toAccountRequest
import uz.tikoncha_parent.data.mapper.protection.toChildRequest
import uz.tikoncha_parent.data.mapper.protection.toProtectionStatus
import uz.tikoncha_parent.data.remote.ProtectionApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.data.remote.model.protection.ChildRequestsResponse
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.protection.AccountRequest
import uz.tikoncha_parent.domain.model.protection.AccountRequestStatus
import uz.tikoncha_parent.domain.model.protection.ChildRequest
import uz.tikoncha_parent.domain.model.protection.ProtectionStatus
import uz.tikoncha_parent.domain.repository.ProtectionRepository

class ProtectionRepositoryImpl(
    private val api: ProtectionApiService,
) : ProtectionRepository {

    override suspend fun protectionStatus(childId: String): Outcome<ProtectionStatus> =
        apiCall(TAG) {
            val r = api.protectionStatus(childId)
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toProtectionStatus())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun strictDisableRequests(
        childId: String?,
        status: String?,
        limit: Int,
        offset: Int,
    ): ChildRequestsResponse {
        return api.strictDisableRequests(childId, status, limit, offset)
    }

    override suspend fun approveStrictDisableRequest(requestId: String): Outcome<ChildRequest> =
        apiCall(TAG) {
            val r = api.approveStrictDisableRequest(requestId)
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toChildRequest())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun rejectStrictDisableRequest(requestId: String): Outcome<ChildRequest> =
        apiCall(TAG) {
            val r = api.rejectStrictDisableRequest(requestId)
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toChildRequest())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun updateAccountRequestStatus(
        requestId: String,
        status: AccountRequestStatus,
    ): Outcome<AccountRequest> =
        apiCall(TAG) {
            val r = api.updateAccountRequestStatus(requestId, status.value)
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.toAccountRequest())
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }
    private companion object { const val TAG = "ProtectionRepository" }

}