package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.parent_requests.UpdateParentRequestResponse
import uz.tikoncha_parent.data.remote.model.parent_requests.ParentRequestsResponse
import uz.tikoncha_parent.data.remote.model.parent_requests.UpdateParentRequest

class ParentRequestsApiService(
    private val client: HttpClient
) {
    suspend fun parentRequests(): ParentRequestsResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "/parent/child-requests",
        )

    suspend fun updateParentRequestStatus(
        requestId: String,
        status: String
    ): UpdateParentRequestResponse =
        client.safeRequest(
            method = HttpMethod.Patch,
            url = "/parent/child-requests/$requestId",
            block = {
               setBody(UpdateParentRequest(status))
            }
        )
}