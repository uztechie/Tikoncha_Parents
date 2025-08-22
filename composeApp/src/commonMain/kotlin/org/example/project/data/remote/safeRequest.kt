package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import org.example.project.data.local.AppSettings
import org.example.project.data.remote.model.RefreshTokenResponse


suspend inline fun <reified T> HttpClient.safeRequest(
    method: HttpMethod,
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): T {
    // First request
    val response = this.request {
        this.method = method
        url(url)
        block()
    }

    // Check for 401
    val bodyText = response.bodyAsText()
    if (bodyText.contains("\"code\":401") || response.status == HttpStatusCode.Unauthorized) {
        val success = refreshAccessToken(this)

        if (success) {
            // Retry request
            val retryResponse = this.request {
                this.method = method
                url(url)
                block()
            }

            return retryResponse.body()
        } else {
            throw Exception("Session expired, please login again.")
        }
    }

    return response.body()
}



suspend inline fun <reified T> HttpClient.safeUploadMultipart(
    url: String,
    formData: List<PartData>
): T {
    // 🔹 1. First attempt
    val response = this.submitFormWithBinaryData(
        url = url,
        formData = formData
    )

    val bodyText = response.bodyAsText()

    // 🔁 2. Token expired (code == 501)
    if (bodyText.contains("\"code\":401")) {
        val success = refreshAccessToken(this)
        if (success) {
            val retryResponse = this.submitFormWithBinaryData(
                url = url,
                formData = formData
            )
            return retryResponse.body()
        } else {
            throw Exception("Session expired. Please log in again.")
        }
    }

    return response.body()
}

suspend fun refreshAccessToken(client: HttpClient): Boolean {
    return try {
        val response = client.post("auth/refresh") {
            parameter("refresh_token", AppSettings.refreshToken)
        }.body<RefreshTokenResponse>()


        if (response.success){
            AppSettings.accessToken = response.data?.access_token?:""
            true
        }
        else{
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}