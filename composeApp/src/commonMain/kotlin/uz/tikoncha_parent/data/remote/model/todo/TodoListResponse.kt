package uz.tikoncha_parent.data.remote.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoListResponse(
    val success: Boolean,
    val data: TodoListData?,
    val error: String? = null,
    val code: Int
)

@Serializable
data class TodoListData(
    val items: List<TodoDto>,
    val total: Int,
    val has_more: Boolean,
    val limit: Int,
    val offset: Int
)

@Serializable
data class EmptyResponse(
    val success: Boolean,
    val error: String? = null,
    val code: Int
)

