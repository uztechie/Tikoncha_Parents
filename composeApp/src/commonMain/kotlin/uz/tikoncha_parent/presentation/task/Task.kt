package uz.tikoncha_parent.presentation.task

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String = "",
    val title: String,
    val description: String,
    val date: String ="",
    val time: String = "",
    val dateTime: Long,
    val importance: ImportanceType,
    val isCompleted: Boolean,
    val isMine: Boolean,
    val authorId: String,
    val targetUserId: String,
    val progress: Int = 0,
    val createdAt: Long
)
