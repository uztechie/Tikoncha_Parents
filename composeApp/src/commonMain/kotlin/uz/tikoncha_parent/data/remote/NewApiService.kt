package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.NewsResponse

class NewApiService (
    private val httpClient: HttpClient,
) {
    suspend fun pushTokenRegister(token: String): NewsResponse =
        httpClient.safeRequest(
            method = HttpMethod.Post,
            url = "notifications/push-token",
            block = { mapOf("token" to token) }
        )

    suspend fun fetchNews(): NewsResponse =
        httpClient.safeRequest(
            method = HttpMethod.Get,
            url = "notifications/news",
            block = {}
        )

    suspend fun markNewsRead(id: Long){
        httpClient.safeRequest<Unit>(
            method = HttpMethod.Post,
            url = "notifications/news/$id/mark-read",
            block = {}
        )
    }

    suspend fun markAllNewsRead(){
        httpClient.safeRequest<Unit>(
            method = HttpMethod.Post,
            url = "notifications/news/mark-all-read",
            block = {}
        )
    }
}