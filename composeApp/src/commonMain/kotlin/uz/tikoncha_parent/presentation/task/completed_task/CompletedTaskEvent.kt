package uz.tikoncha_parent.presentation.task.completed_task

import uz.tikoncha_parent.domain.model.app_error.Outcome

sealed interface CompletedTaskEvent {
    data object LoadTasks : CompletedTaskEvent
    data object OnRefresh : CompletedTaskEvent
    data object OnLoadMore : CompletedTaskEvent
    data class OnTabSelected(val taskIndex: Int) : CompletedTaskEvent
}

sealed interface CompletedTaskEffect {
    data class ShowFailure(val failure: Outcome.Failure) : CompletedTaskEffect
}