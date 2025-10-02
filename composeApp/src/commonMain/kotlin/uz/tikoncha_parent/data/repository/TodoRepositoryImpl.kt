package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.TodoApiService
import uz.tikoncha_parent.data.remote.model.TodoListResponse
import uz.tikoncha_parent.data.remote.model.TodoRequest
import uz.tikoncha_parent.data.remote.model.TodoResponse
import uz.tikoncha_parent.domain.repository.TodoRepository

class TodoRepositoryImpl(private val api: TodoApiService): TodoRepository {

    override suspend fun registerTodo(request: TodoRequest): TodoResponse {
        return api.registerTodo(request)
    }

    override suspend fun getTodoList(userId: String): TodoListResponse {
        return api.todoList(userId)
    }

    override suspend fun updateTodo(request: TodoRequest): TodoResponse {
        return api.updateTodo(request)
    }

}