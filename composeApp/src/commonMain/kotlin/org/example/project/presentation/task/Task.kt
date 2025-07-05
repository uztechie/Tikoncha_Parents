package org.example.project.presentation.task

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val dateTime: Long,
    val importance: ImportanceType,
    val isCompleted: Boolean,
    val progress:Int,
    val createdAt: Long
)
