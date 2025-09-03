package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TodoRequest(
    val id: String,
    val title: String,
    val description: String?,
    val created_at: String,
    val due_date: String?,
    val importance: String,
    val target_user_id: String?,
    val is_completed: Boolean?
)
