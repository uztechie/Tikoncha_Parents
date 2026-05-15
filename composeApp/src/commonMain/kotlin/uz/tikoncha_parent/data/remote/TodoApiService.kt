package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.remote.model.*
import uz.tikoncha_parent.data.remote.model.todo.CreateTodoRequest
import uz.tikoncha_parent.data.remote.model.todo.EmptyResponse
import uz.tikoncha_parent.data.remote.model.todo.TodoListResponse
import uz.tikoncha_parent.data.remote.model.todo.TodoResponse
import uz.tikoncha_parent.data.remote.model.todo.UpdateTodoRequest

class TodoApiService(private val client: HttpClient) {

    suspend fun listTodos(
        targetUserId: String?,
        isCompleted: Boolean?,
        isChildDone: Boolean?,
        isExpired: Boolean?,
        importance: String?,
        orderBy: String = "created_at",
        order: String = "desc",
        limit: Int,
        offset: Int
    ): TodoListResponse = client.safeRequest(
        method = HttpMethod.Get,
        url = "todos",
        block = {
            targetUserId?.let { parameter("target_user_id", it) }
            isCompleted?.let { parameter("is_completed", it) }
            isChildDone?.let { parameter("is_child_done", it) }
            isExpired?.let { parameter("is_expired", it) }
            importance?.let { parameter("importance", it) }
            parameter("order_by", orderBy)
            parameter("order", order)
            parameter("limit", limit)
            parameter("offset", offset)
        }
    )

    suspend fun getTodoById(id: String): TodoResponse = client.safeRequest(
        method = HttpMethod.Get,
        url = "todos/$id"
    )

    suspend fun createTodo(request: CreateTodoRequest): TodoResponse = client.safeRequest(
        method = HttpMethod.Post,
        url = "todos",
        block = { setBody(request) }
    )

    suspend fun updateTodo(id: String, request: UpdateTodoRequest): TodoResponse =
        client.safeRequest(
            method = HttpMethod.Patch,
            url = "todos/$id",
            block = { setBody(request) }
        )

    suspend fun deleteTodo(id: String): EmptyResponse = client.safeRequest(
        method = HttpMethod.Delete,
        url = "todos/$id"
    )

    /** Parent tomonidan "Tekshirildi" — POST /todos/{id}/complete */
    suspend fun completeTodo(id: String): TodoResponse = client.safeRequest(
        method = HttpMethod.Post,
        url = "todos/$id/complete"
    )
}