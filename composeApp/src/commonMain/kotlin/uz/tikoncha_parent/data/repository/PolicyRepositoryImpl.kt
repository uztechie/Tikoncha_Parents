package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.PolicyApiService
import uz.tikoncha_parent.data.remote.model.GetPoliciesResponse
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.domain.repository.PolicyRepository

class PolicyRepositoryImpl(private val api: PolicyApiService): PolicyRepository {
    override suspend fun getPolicies(userId: String): GetPoliciesResponse {
        return api.getPolicies(userId)
    }

    override suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse {
        return api.createPolicy(createPolicyRequest)
    }

}