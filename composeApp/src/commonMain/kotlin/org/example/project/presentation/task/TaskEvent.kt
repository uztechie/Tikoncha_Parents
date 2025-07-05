package org.example.project.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


sealed interface TaskEvent {
    data class OnTitleChange(val title: String) : TaskEvent
    data class OnDescChange(val desc: String) : TaskEvent
    data class OnDateChange(val date: LocalDate) : TaskEvent
    data class OnTimeChange(val time: LocalTime) : TaskEvent
    data class OnImportanceChange(val importance: ImportanceType) : TaskEvent
}