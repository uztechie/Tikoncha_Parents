package org.example.project.presentation.completedTask

data class CompletedTaskState(
    val genderIndex: Int = 0,
    val newTasks: List<CompletedTaskList> = emptyList()
)