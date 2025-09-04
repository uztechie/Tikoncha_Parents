package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.TodoListResponse
import uz.tikoncha_parent.data.remote.model.TodoRequest
import uz.tikoncha_parent.data.remote.model.TodoResponse

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