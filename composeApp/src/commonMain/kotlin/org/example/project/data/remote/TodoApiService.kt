package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import org.example.project.data.local.AppSettings
import org.example.project.data.remote.model.TodoListResponse
import org.example.project.data.remote.model.TodoRequest
import org.example.project.data.remote.model.TodoResponse
import org.example.project.data.remote.model.UserInfoResponse

class TodoApiService(private val client: HttpClient) {

    suspend fun registerTodo(request: TodoRequest): TodoResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "todos",
            block = {
                setBody(request)
            }
        )

    suspend fun todoList(userId: String): TodoListResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "todos",
            block = {
                parameter("target_user_id", userId)
            }
        )
}