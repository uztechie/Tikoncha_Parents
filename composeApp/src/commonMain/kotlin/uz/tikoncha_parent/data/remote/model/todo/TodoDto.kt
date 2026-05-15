package uz.tikoncha_parent.data.remote.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoDto(
    val id: String,
    val author_id: String?,
    val target_user_id: String?,
    val title: String,
    val description: String,
    val due_date: Long?,
    val importance: String,
    val is_child_done: Boolean,
    val is_completed: Boolean,
    val is_expired: Boolean,
    val created_at: Long?,
    val modified_at: Long?,
    val coin: Int = 0
)