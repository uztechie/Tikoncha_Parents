package uz.tikoncha_parent.data.remote.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoRequest(
    val id: String,
    val title: String,
    val description: String?,
    val created_at: Long,
    val due_date: Long?,
    val importance: String,
    val target_user_id: String?,
    val is_completed: Boolean?,
    val coin: Int = 0
)