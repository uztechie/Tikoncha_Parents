package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.AddChildRequest
import uz.tikoncha_parent.data.remote.model.AddChildResponse
import uz.tikoncha_parent.data.remote.model.AppUsageResponse
import uz.tikoncha_parent.data.remote.model.ChildrenLocationResponse
import uz.tikoncha_parent.data.remote.model.ChildrenResponse
import uz.tikoncha_parent.data.remote.model.UnlinkChildRequest
import uz.tikoncha_parent.data.remote.model.UnlinkChildResponse

class ChildApiService(private val client: HttpClient) {

    suspend fun addChild(request: AddChildRequest): AddChildResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "users/add-child",
            block = {
                setBody(request)
            }
        )

    suspend fun children(): ChildrenResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "users/children",
            block = {

            }
        )
    suspend fun appUsages(params: Map<String, Any>): AppUsageResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "data-exchange",
            block = {
                params.forEach {
                    parameter(it.key, it.value)
                }
            }
        )

    suspend fun childrenLocation(): ChildrenLocationResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "data-exchange/children/locations",
            block = {}
        )

    suspend fun unlinkChild(request: UnlinkChildRequest): UnlinkChildResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "users/unlink-parent",
            block = {
                setBody(request)
            }
        )
}