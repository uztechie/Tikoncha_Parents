package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.AvatarResponse
import uz.tikoncha_parent.domain.model.UploadPart

class AvatarApiService(private val client: HttpClient) {
    suspend fun uploadAvatar(part: UploadPart): AvatarResponse =
        client.safeUploadMultipart<AvatarResponse>(
        url = "users/avatar/",
        formData = formData {
            append(
                key = "file",
                value = part.bytes,
                Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(HttpHeaders.ContentDisposition, "filename=\"${part.fileName}\"")
                }
            )
        }
    )

    suspend fun loadAvatar(): AvatarResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "users/avatar/",
            block = {}
        )
}