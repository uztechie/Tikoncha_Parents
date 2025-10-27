package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class TodoDto(
    val id: String,
    val author_id: String?,
    val target_user_id: String?,
    val title: String,
    val description: String,
    val due_date: String,
    val importance: String,
    val is_completed: Boolean,
    val created_at: String?,
    val modified_at: String?,
    val coin: Int = 0
)
