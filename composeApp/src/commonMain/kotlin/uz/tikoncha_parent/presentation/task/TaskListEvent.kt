package uz.tikoncha_parent.presentation.task

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.task.model.Task

sealed interface TaskListEvent {
    // ✅ Yuklash & pagination
    data object LoadTasks : TaskListEvent
    data object OnRefresh : TaskListEvent
    data object OnLoadMore : TaskListEvent
    data object OnRetry : TaskListEvent
    data object LoadAllChildrenActiveTasks : TaskListEvent
    data object ClearError : TaskListEvent

    // ✅ Filter
    data class OnFilterChipToggled(val chip: TaskFilterChip) : TaskListEvent

    // Toggles & tabs
    data object ShowMineAll : TaskListEvent
    data object ShowChildrenAll : TaskListEvent
    data class OnChildSelected(val child: UserInfo) : TaskListEvent
    data class OnTaskSelected(val taskIndex: Int) : TaskListEvent

    // Actions
    data class OnCompletedTask(val task: Task) : TaskListEvent
    data class OnDeleteTask(val task: Task) : TaskListEvent
}