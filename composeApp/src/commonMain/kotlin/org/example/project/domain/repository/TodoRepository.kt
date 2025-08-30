package org.example.project.domain.repository

import io.ktor.http.cio.Response
import org.example.project.data.remote.model.TodoDto
import org.example.project.data.remote.model.TodoListResponse
import org.example.project.data.remote.model.TodoRequest
import org.example.project.data.remote.model.TodoResponse

interface TodoRepository {
    suspend fun registerTodo(request: TodoRequest): TodoResponse

    suspend fun getTodoList(userId: String): TodoListResponse
}