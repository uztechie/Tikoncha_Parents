package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.data.remote.model.CreateRuleRequest
import uz.tikoncha_parent.data.remote.model.CreateRuleResponse
import uz.tikoncha_parent.data.remote.model.GetRulesResponse
import uz.tikoncha_parent.data.remote.model.UpsertRuleRequest
import uz.tikoncha_parent.data.remote.model.UpsertRuleResponse

interface RulesRepository {

    suspend fun getRules(studentId: String): GetRulesResponse
    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse
    suspend fun createRule(createRuleRequest: CreateRuleRequest): CreateRuleResponse
    suspend fun upsertRule(upsertRuleRequest: UpsertRuleRequest): UpsertRuleResponse
}