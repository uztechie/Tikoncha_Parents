package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.AppsResponse
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.data.remote.model.DeletePolicyResponse
import uz.tikoncha_parent.data.remote.model.PolicyResponse
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.data.remote.model.UpdatePolicyResponse

interface PolicyRepository {

    suspend fun childApps(userId: String): AppsResponse
    suspend fun getPolicies(userId: String): PolicyResponse
    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse

    suspend fun updatePolicyInServer(updatePolicyRequest: UpdatePolicyRequest, ruleId: String): UpdatePolicyResponse

    suspend fun deletePolicyInServer(ruleId: String): DeletePolicyResponse

}