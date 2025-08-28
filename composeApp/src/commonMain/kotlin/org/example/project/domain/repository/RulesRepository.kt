package org.example.project.domain.repository

import org.example.project.data.remote.model.AddChildRequest
import org.example.project.data.remote.model.AddChildResponse
import org.example.project.data.remote.model.AppUsageResponse
import org.example.project.data.remote.model.ChildrenResponse
import org.example.project.data.remote.model.CreatePolicyRequest
import org.example.project.data.remote.model.CreatePolicyResponse
import org.example.project.data.remote.model.CreateRuleRequest
import org.example.project.data.remote.model.CreateRuleResponse
import org.example.project.data.remote.model.GetRulesResponse
import org.example.project.data.remote.model.UpsertRuleRequest
import org.example.project.data.remote.model.UpsertRuleResponse

interface RulesRepository {

    suspend fun getRules(studentId: String): GetRulesResponse
    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse
    suspend fun createRule(createRuleRequest: CreateRuleRequest): CreateRuleResponse
    suspend fun upsertRule(upsertRuleRequest: UpsertRuleRequest): UpsertRuleResponse
}