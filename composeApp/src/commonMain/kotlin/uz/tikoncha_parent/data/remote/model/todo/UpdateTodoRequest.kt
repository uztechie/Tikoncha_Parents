package uz.tikoncha_parent.data.remote.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTodoRequest(
    val title: String?,
    val description: String?,
    val due_date: Long?,
    val importance: String?,
    val coin: Int?
)