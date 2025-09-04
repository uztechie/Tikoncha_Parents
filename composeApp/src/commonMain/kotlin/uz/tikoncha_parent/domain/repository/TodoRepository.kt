package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.TodoListResponse
import uz.tikoncha_parent.data.remote.model.TodoRequest
import uz.tikoncha_parent.data.remote.model.TodoResponse

interface TodoRepository {
    suspend fun registerTodo(request: TodoRequest): TodoResponse

    suspend fun getTodoList(userId: String): TodoListResponse
}