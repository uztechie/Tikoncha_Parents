package org.example.project.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.example.project.presentation.completedTask.CompletedTaskEvent
import org.example.project.presentation.home.HomeEvent


sealed interface TaskEvent {
    data class OnTitleChange(val title: String) : TaskEvent
    data class OnDescChange(val desc: String) : TaskEvent
    data class OnDateChange(val date: LocalDate) : TaskEvent
    data class OnTimeChange(val time: LocalTime) : TaskEvent
    data class OnImportanceChange(val importance: ImportanceType) : TaskEvent
    data class OnChildSelected(val child: String): TaskEvent
    data class OnGenderSelected(val genderIndex: Int): TaskEvent


}