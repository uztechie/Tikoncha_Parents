package uz.tikoncha_parent.presentation.task.completed_task

sealed interface CompletedTaskEvent {
    data object LoadTasks : CompletedTaskEvent
    data object OnRefresh : CompletedTaskEvent
    data object OnLoadMore : CompletedTaskEvent
    data object ClearError : CompletedTaskEvent
    data class OnTabSelected(val taskIndex: Int) : CompletedTaskEvent
}

sealed interface CompletedTaskEffect {
    data class ShowError(val message: String) : CompletedTaskEffect
}