package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.RulesApiService
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.data.remote.model.CreateRuleRequest
import uz.tikoncha_parent.data.remote.model.CreateRuleResponse
import uz.tikoncha_parent.data.remote.model.GetRulesResponse
import uz.tikoncha_parent.data.remote.model.UpsertRuleRequest
import uz.tikoncha_parent.data.remote.model.UpsertRuleResponse
import uz.tikoncha_parent.domain.repository.RulesRepository

class RulesRepositoryImpl(private val api: RulesApiService): RulesRepository {
    override suspend fun getRules(userId: String): GetRulesResponse {
        return api.getRules(userId)
    }

    override suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse {
        println("RulesRepositoryImpl createPolicy=$createPolicyRequest")

        return api.createPolicy(createPolicyRequest)
    }

    override suspend fun createRule(createRuleRequest: CreateRuleRequest): CreateRuleResponse {
        return api.createRule(createRuleRequest)
    }

    override suspend fun upsertRule(upsertRuleRequest: UpsertRuleRequest): UpsertRuleResponse {
        return api.upsertRule(upsertRuleRequest)
    }


}