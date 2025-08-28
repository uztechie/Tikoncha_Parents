package org.example.project.data.repository

import org.example.project.data.remote.RulesApiService
import org.example.project.data.remote.model.CreatePolicyRequest
import org.example.project.data.remote.model.CreatePolicyResponse
import org.example.project.data.remote.model.CreateRuleRequest
import org.example.project.data.remote.model.CreateRuleResponse
import org.example.project.data.remote.model.GetRulesResponse
import org.example.project.data.remote.model.UpsertRuleRequest
import org.example.project.data.remote.model.UpsertRuleResponse
import org.example.project.domain.repository.RulesRepository

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