package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import org.example.project.data.remote.model.AddChildRequest
import org.example.project.data.remote.model.AddChildResponse
import org.example.project.data.remote.model.ChildrenResponse
import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.saidburxon.newedu.data.model.SendOtpResponse

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

}