package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.PolicyApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.data.remote.model.AppDto
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.PolicyDto
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.domain.model.app_error.ErrorCause
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.PolicyRepository

class PolicyRepositoryImpl(private val api: PolicyApiService): PolicyRepository {
    override suspend fun childApps(userId: String): Outcome<List<AppDto>> =
        apiCall(TAG) {
            val r = api.childApps(userId)
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.items)
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun getPolicies(userId: String): Outcome<List<PolicyDto>> =
        apiCall(TAG) {
            val r = api.getPolicies(userId)
            val body = r.data
            when {
                r.success && body != null -> Outcome.Success(body.policies)
                r.success -> Outcome.Failure(ErrorCause.InvalidResponse)
                else -> Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
            }
        }

    override suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): Outcome<Unit> =
        apiCall(TAG) {
            val r = api.createPolicy(createPolicyRequest)
            if (r.success) Outcome.Success(Unit)
            else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
        }

    override suspend fun updatePolicyInServer(
        updatePolicyRequest: UpdatePolicyRequest,
        ruleId: String,
    ): Outcome<Unit> =
        apiCall(TAG) {
            val r = api.updatePolicy(updatePolicyRequest, ruleId)
            if (r.success) Outcome.Success(Unit)
            else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
        }

    override suspend fun deletePolicyInServer(ruleId: String): Outcome<Unit> =
        apiCall(TAG) {
            val r = api.deletePolicy(ruleId)
            if (r.success) Outcome.Success(Unit)
            else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
        }

    private companion object { const val TAG = "PolicyRepository" }
}