package uz.tikoncha_parent.data.remote.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoResponse(
    val success: Boolean,
    val data: TodoDto?,
    val error: String? = null,
    val code:Int
)
