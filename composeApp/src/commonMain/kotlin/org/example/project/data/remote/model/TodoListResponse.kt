package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TodoListResponse(
    val success: Boolean,
    val data: TodoData?,
    val error: String? = null,
    val code:Int
)

@Serializable
data class TodoData(
    val items: List<TodoDto>
)

