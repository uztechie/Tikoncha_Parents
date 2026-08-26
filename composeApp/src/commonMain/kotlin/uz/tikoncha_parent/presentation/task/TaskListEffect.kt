package uz.tikoncha_parent.presentation.task

import uz.tikoncha_parent.domain.model.app_error.Outcome

sealed interface TaskListEffect {
    data class ShowFailure(val failure: Outcome.Failure) : TaskListEffect
    data object TaskMarkedAsCompleted : TaskListEffect
    data object TaskDeleted : TaskListEffect
}