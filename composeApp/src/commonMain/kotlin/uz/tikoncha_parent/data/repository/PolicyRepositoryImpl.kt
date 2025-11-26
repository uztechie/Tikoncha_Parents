package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.PolicyApiService
import uz.tikoncha_parent.data.remote.model.AppsResponse
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.data.remote.model.DeletePolicyResponse
import uz.tikoncha_parent.data.remote.model.PolicyResponse
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.data.remote.model.UpdatePolicyResponse
import uz.tikoncha_parent.domain.repository.PolicyRepository

class PolicyRepositoryImpl(private val api: PolicyApiService): PolicyRepository {
    override suspend fun childApps(userId: String): AppsResponse {
        return api.childApps(userId)
    }

    override suspend fun getPolicies(userId: String): PolicyResponse {
        return api.getPolicies(userId)
    }

    override suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse {
        return api.createPolicy(createPolicyRequest)
    }

    override suspend fun updatePolicyInServer(
        updatePolicyRequest: UpdatePolicyRequest,
        ruleId: String
    ): UpdatePolicyResponse {
        return api.updatePolicy(updatePolicyRequest, ruleId)
    }

    override suspend fun deletePolicyInServer(ruleId: String): DeletePolicyResponse {
        return api.deletePolicy(ruleId = ruleId)
    }

}