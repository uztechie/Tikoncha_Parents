package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.data.remote.model.CreateRuleRequest
import uz.tikoncha_parent.data.remote.model.CreateRuleResponse
import uz.tikoncha_parent.data.remote.model.GetRulesResponse
import uz.tikoncha_parent.data.remote.model.UpsertRuleRequest
import uz.tikoncha_parent.data.remote.model.UpsertRuleResponse

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

    suspend fun upsertRule(upsertRuleRequest: UpsertRuleRequest): UpsertRuleResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "policies/parent/rule-upsert",
            block = {
                setBody(upsertRuleRequest)
            }
        )



    suspend fun getRules(userId: String): GetRulesResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "policies/evaluate/apps-effective",
            block = {
                parameter("user_id", userId)

            }
        )




}