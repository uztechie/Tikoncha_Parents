package org.example.project.presentation.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.example.project.domain.model.UserInfo


sealed interface TaskEvent {
    object OnConfirmClicked: TaskEvent
    object OnReset: TaskEvent
    object LoadTasks: TaskEvent
    object GetChildren: TaskEvent
    object ShowMineAll : TaskEvent

    data class OnCompletedTask(val task: Task): TaskEvent
    data class OnTitleChange(val title: String) : TaskEvent
    data class OnDescChange(val desc: String) : TaskEvent
    data class OnDateChange(val date: LocalDate) : TaskEvent
    data class OnTimeChange(val time: LocalTime) : TaskEvent
    data class OnImportanceChange(val importance: ImportanceType) : TaskEvent
    data class OnChildSelected(val child: UserInfo): TaskEvent
    data class OnGenderSelected(val genderIndex: Int): TaskEvent
}