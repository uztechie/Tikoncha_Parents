package org.example.project.data.repository

import org.example.project.data.remote.TodoApiService
import org.example.project.data.remote.model.TodoListResponse
import org.example.project.data.remote.model.TodoRequest
import org.example.project.data.remote.model.TodoResponse
import org.example.project.domain.repository.TodoRepository

class TodoRepositoryImpl(private val api: TodoApiService): TodoRepository {

    override suspend fun registerTodo(request: TodoRequest): TodoResponse {
        return api.registerTodo(request)
    }

    override suspend fun getTodoList(userId: String): TodoListResponse {
        return api.todoList(userId)
    }

}