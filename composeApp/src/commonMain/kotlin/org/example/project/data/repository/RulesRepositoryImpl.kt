package org.example.project.data.repository

import org.example.project.data.remote.RulesApiService
import org.example.project.data.remote.model.CreatePolicyRequest
import org.example.project.data.remote.model.CreatePolicyResponse
import org.example.project.data.remote.model.CreateRuleRequest
import org.example.project.data.remote.model.CreateRuleResponse
import org.example.project.data.remote.model.GetRulesResponse
import org.example.project.domain.repository.RulesRepository

class RulesRepositoryImpl(private val api: RulesApiService): RulesRepository {
    override suspend fun getRules(studentId: String): GetRulesResponse {
        return api.getRules(studentId)
    }

    override suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse {
        println("RulesRepositoryImpl createPolicy=$createPolicyRequest")

        return api.createPolicy(createPolicyRequest)
    }

    override suspend fun createRule(createRuleRequest: CreateRuleRequest): CreateRuleResponse {
        return api.createRule(createRuleRequest)
    }


}