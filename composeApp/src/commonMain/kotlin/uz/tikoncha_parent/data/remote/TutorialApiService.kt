package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusRequest
import uz.tikoncha_parent.data.remote.model.permission_status.PermissionStatusResponse
import uz.tikoncha_parent.data.remote.model.tutorial.VideoTutorialResponse

class TutorialApiService(
    private val httpClient: HttpClient,
) {
    suspend fun videoTutorials(): VideoTutorialResponse =
        httpClient.safeRequest(
            method = HttpMethod.Get,
            url = "/tutorials",
            block = {}
        )
}