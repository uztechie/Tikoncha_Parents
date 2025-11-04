package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.CreatePolicyRequestTemp
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponseTemp
import uz.tikoncha_parent.data.remote.model.CreateRuleRequest
import uz.tikoncha_parent.data.remote.model.CreateRuleResponse
import uz.tikoncha_parent.data.remote.model.GetRulesResponse
import uz.tikoncha_parent.data.remote.model.UpsertRuleRequest
import uz.tikoncha_parent.data.remote.model.UpsertRuleResponse

interface RulesRepository {

    suspend fun getRules(studentId: String): GetRulesResponse
    suspend fun createPolicy(createPolicyRequestTemp: CreatePolicyRequestTemp): CreatePolicyResponseTemp
    suspend fun createRule(createRuleRequest: CreateRuleRequest): CreateRuleResponse
    suspend fun upsertRule(upsertRuleRequest: UpsertRuleRequest): UpsertRuleResponse
}