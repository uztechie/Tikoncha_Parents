package org.example.project.presentation.completedTask

import org.example.project.presentation.task.ImportanceType
import utils.getCurrentTimeMillis

data class CompletedTaskList(
    val id: Long = 0,
    val task: String = "Kitob o’qish",
    val content: String = "Bir hafta davomida xar kuni 10 varoqdan",
    val endDate: String = "04.11.2025",
    val endTime: String = "08:00",
    val importance: ImportanceType = ImportanceType.IMPORTANT,
    val createdAt: Long = getCurrentTimeMillis(),
    val progress:Int = 80,
    val isCompleted: Boolean = false
)