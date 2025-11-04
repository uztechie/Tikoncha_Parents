package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.data.remote.model.GetPoliciesResponse

class PolicyApiService(private val client: HttpClient) {



    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "/policies/create-policy-rule",
            block = {
                println("createPolicy=$createPolicyRequest")
                setBody(createPolicyRequest)
            }
        )


    suspend fun getPolicies(userId: String): GetPoliciesResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "/policies/active-policies",
            block = {
                parameter("user_id", userId)
            }
        )




}