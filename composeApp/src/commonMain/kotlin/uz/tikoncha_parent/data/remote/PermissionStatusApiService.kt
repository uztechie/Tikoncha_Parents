package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusResponse

class PermissionStatusApiService(
    private val httpClient: HttpClient,
) {
    suspend fun permissionStatus(request: PermissionStatusRequest): PermissionStatusResponse =
        httpClient.safeRequest(
            method = HttpMethod.Get,
            url = "/users/children/mode-status/${request.userId}",
            block = {
                parameter("state", request.state)
            }
        )
}