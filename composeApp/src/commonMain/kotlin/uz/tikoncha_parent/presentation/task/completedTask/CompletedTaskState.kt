package uz.tikoncha_parent.presentation.task.completedTask

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.task.model.Task

data class CompletedTaskState(
    // Child & user
    val selectedChild: UserInfo? = null,
    val userId: String? = null,

    // Tab: 0 = Mendan (PARENT), 1 = Farzandim (CHILD)
    val taskIndex: Int = 0,

    // List
    val taskList: List<Task> = emptyList(),
    val totalCount: Int = 0,

    // Pagination
    val hasMore: Boolean = true,
    val offset: Int = 0,
    val pageSize: Int = 20,

    // Loading states
    val isInitialLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaginating: Boolean = false,
    val isRefiltering: Boolean = false,

    // Error
    val errorMessage: String? = null,
) {
    val canFetch: Boolean
        get() = selectedChild != null
}