package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.logout.LogoutResponse

class LogoutApiService(
    private val client: HttpClient
) {
    suspend fun getLogout(): LogoutResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "/parent/child-requests",
        )
}