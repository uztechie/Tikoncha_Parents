package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.AppDto
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.PolicyDto
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.domain.model.app_error.Outcome

interface PolicyRepository {

    suspend fun childApps(userId: String): Outcome<List<AppDto>>
    suspend fun getPolicies(userId: String): Outcome<List<PolicyDto>>
    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): Outcome<Unit>
    suspend fun updatePolicyInServer(updatePolicyRequest: UpdatePolicyRequest, ruleId: String): Outcome<Unit>
    suspend fun deletePolicyInServer(ruleId: String): Outcome<Unit>
}