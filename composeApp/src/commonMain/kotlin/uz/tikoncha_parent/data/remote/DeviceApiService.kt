package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.data.remote.model.DeviceRegisterResponse

class DeviceApiService (
    private val httpClient: HttpClient
) {
    suspend fun registerDevice(request: DeviceRegisterRequest): DeviceRegisterResponse =
        httpClient.safeRequest(
            method = HttpMethod.Post,
            url = "devices/register",
            block = {
                setBody(request)
            }
        )
}