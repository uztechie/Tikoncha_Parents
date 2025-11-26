package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.AppsResponse
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreatePolicyResponse
import uz.tikoncha_parent.data.remote.model.DeletePolicyResponse
import uz.tikoncha_parent.data.remote.model.PolicyResponse
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.data.remote.model.UpdatePolicyResponse

class PolicyApiService(private val client: HttpClient) {

    suspend fun childApps(userId: String): AppsResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "/installed-apps",
            block = {
                parameter("user_id", userId)
            }
        )



    suspend fun createPolicy(createPolicyRequest: CreatePolicyRequest): CreatePolicyResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "/policies/create-policy-rule",
            block = {
                println("createPolicy=$createPolicyRequest")
                setBody(createPolicyRequest)
            }
        )

    suspend fun updatePolicy(updatePolicyRequest: UpdatePolicyRequest, ruleId: String): UpdatePolicyResponse =
        client.safeRequest(
            method = HttpMethod.Put,
            url = "/policies/rules/$ruleId",
            block = {
                println("createPolicy=$updatePolicyRequest")
                setBody(updatePolicyRequest)
            }
        )

    suspend fun deletePolicy(ruleId: String): DeletePolicyResponse =
        client.safeRequest(
            method = HttpMethod.Delete,
            url = "/policies/rules/$ruleId",
            block = {
                parameter("rule_id", ruleId)
            }
        )




    suspend fun getPolicies(userId: String): PolicyResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "policies/active-policies",
            block = {
                parameter("user_id", userId)
            }
        )







}