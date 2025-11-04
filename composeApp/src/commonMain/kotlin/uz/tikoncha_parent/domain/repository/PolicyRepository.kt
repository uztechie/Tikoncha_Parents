package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.GetPoliciesResponse
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse

interface PolicyRepository {
    suspend fun getPolicies(userId: String): GetPoliciesResponse
    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse

}