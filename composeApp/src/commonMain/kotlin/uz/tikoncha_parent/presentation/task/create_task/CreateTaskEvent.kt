package uz.tikoncha_parent.presentation.task.create_task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.task.model.ImportanceType
import uz.tikoncha_parent.presentation.task.model.Task

sealed interface CreateTaskEvent {
    data object OnConfirmClicked : CreateTaskEvent
    data object OnReset : CreateTaskEvent
    data object LoadParentCoins : CreateTaskEvent

    data class OnTitleChange(val title: String) : CreateTaskEvent
    data class OnDescChange(val desc: String) : CreateTaskEvent
    data class OnDateChange(val date: LocalDate) : CreateTaskEvent
    data class OnTimeChange(val time: LocalTime) : CreateTaskEvent
    data class OnImportanceChange(val importance: ImportanceType) : CreateTaskEvent
    data class OnChildSelected(val child: UserInfo) : CreateTaskEvent
    data class OnEditTask(val task: Task) : CreateTaskEvent
    data class OnChipToggle(val value: Int) : CreateTaskEvent
    data class OnExtraCoinChange(val value: Int) : CreateTaskEvent
}