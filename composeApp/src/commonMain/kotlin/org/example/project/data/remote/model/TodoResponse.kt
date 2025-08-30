package org.example.project.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TodoResponse(
    val success: Boolean,
    val data: TodoDto?,
    val error: String? = null,
    val code:Int
)
