package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import network.chaintech.cmpimagepickncrop.imagecropper.polygonPath
import org.example.project.data.remote.model.CreatePolicyRequest
import org.example.project.data.remote.model.CreatePolicyResponse
import org.example.project.data.remote.model.CreateRuleRequest
import org.example.project.data.remote.model.CreateRuleResponse
import org.example.project.data.remote.model.GetRulesResponse

class RulesApiService(private val client: HttpClient) {



    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "/policies/",
            block = {
                println("createPolicy=$createPolicyRequest")
                setBody(createPolicyRequest)
            }
        )

    suspend fun createRule(createRuleRequest: CreateRuleRequest): CreateRuleResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "policies/${createRuleRequest.policyId}/rules",
            block = {
                setBody(createRuleRequest)
            }
        )


    suspend fun getRules(studentId: String): GetRulesResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "policies/students/${studentId}/effective",
            block = {}
        )





}