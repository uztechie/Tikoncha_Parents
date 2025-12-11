package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.parent_requests.ParentRequestsResponse

class ParentRequestsApiService(
    private val client: HttpClient
) {
    suspend fun parentRequests(): ParentRequestsResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "/parent/child-requests",
        )
}