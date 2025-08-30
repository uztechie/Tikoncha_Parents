package org.example.project.presentation.task

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val dateTime: Long,
    val importance: ImportanceType,
    val is_completed: Boolean,
    val progress: Int,
    val created_at: Long
)
