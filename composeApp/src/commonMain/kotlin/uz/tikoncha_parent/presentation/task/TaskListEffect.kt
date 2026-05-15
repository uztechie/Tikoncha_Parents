package uz.tikoncha_parent.presentation.task

sealed interface TaskListEffect {
    data class ShowError(val message: String) : TaskListEffect
    data class ShowMessage(val message: String) : TaskListEffect
    data object TaskMarkedAsCompleted : TaskListEffect
    data object TaskDeleted : TaskListEffect
}