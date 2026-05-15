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

    // ✅ Filter
    data class OnFilterChipToggled(val chip: TaskFilterChip) : TaskListEvent

    // Toggles & tabs
    data object ShowMineAll : TaskListEvent
    data object ShowChildrenAll : TaskListEvent
    data class OnChildSelected(val child: UserInfo) : TaskListEvent
    data class OnTaskSelected(val taskIndex: Int) : TaskListEvent
    data class OnGenderSelected(val genderIndex: Int) : TaskListEvent

    // Actions
    data class OnCompletedTask(val task: Task) : TaskListEvent       // "Tekshirildi"
    data class OnDeleteTask(val task: Task) : TaskListEvent

    data object LoadCompletedTasks : TaskListEvent
    data object LoadMoreCompleted : TaskListEvent
}