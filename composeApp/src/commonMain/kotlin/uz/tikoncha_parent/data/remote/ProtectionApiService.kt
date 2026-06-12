package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.protection.AccountRequestActionRequest
import uz.tikoncha_parent.data.remote.model.protection.AccountRequestActionResponse
import uz.tikoncha_parent.data.remote.model.protection.ChildRequestsResponse
import uz.tikoncha_parent.data.remote.model.protection.ProtectionStatusResponse
import uz.tikoncha_parent.data.remote.model.protection.RequestActionResponse

class ProtectionApiService(private val client: HttpClient) {

    suspend fun protectionStatus(childId: String): ProtectionStatusResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "/mode/protection-status",
            block = {
                parameter("child_id", childId)
            }
        )

    suspend fun strictDisableRequests(
        childId: String? = null,
        status: String? = null,
        limit: Int = 20,
        offset: Int = 0,
    ): ChildRequestsResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "/mode/strict-disable-requests",
            block = {
                childId?.let { parameter("child_id", it) }
                status?.let { parameter("status", it) }
                parameter("limit", limit)
                parameter("offset", offset)
            }
        )

    suspend fun approveStrictDisableRequest(requestId: String): RequestActionResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "/mode/strict-disable-request/$requestId/approve",
            block = {}
        )

    suspend fun rejectStrictDisableRequest(requestId: String): RequestActionResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "/mode/strict-disable-request/$requestId/reject",
            block = {}
        )

    /** Hisobdan chiqish / ilovani o'chirish so'roviga javob: access | deny */
    suspend fun updateAccountRequestStatus(
        requestId: String,
        status: String,
    ): AccountRequestActionResponse =
        client.safeRequest(
            method = HttpMethod.Patch,
            url = "/parent/child-requests/$requestId",
            block = {
                setBody(AccountRequestActionRequest(status))
            }
        )
}