package org.example.project.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.example.project.presentation.completedTask.CompletedTaskList


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
    val completedTasksEndList: List<CompletedTaskList> = emptyList(),
    val completedTasksFull: List<Task> = emptyList(),
    val childList: List<String> = listOf("Saidburkhon","Abror","Axror","Muhtor","Asror","Anvar"),
    val child: String = childList[0],
    val genderIndex: Int = 0,
)
