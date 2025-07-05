package org.example.project.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


data class TaskState(
    var title: String = "",
    var desc: String = "",
    var date: LocalDate? = null,
    var time: LocalTime? = null,
    var importance: ImportanceType = ImportanceType.NONE,
    var isSaveButtonEnabled: Boolean = false,
    var completed: Boolean? = null,
    var showMineAll: Boolean = false,
    var task: Task? = null,
    val newTasksFull: List<Task> = emptyList(),
    val newTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val completedTasksFull: List<Task> = emptyList(),
)
