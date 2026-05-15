package uz.tikoncha_parent.presentation.task

import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.task.model.Task
import uz.tikoncha_parent.presentation.ui_state.ResponseState

enum class TaskFilterChip { IN_PROGRESS, DONE_BY_CHILD, OVERDUE }

data class TaskListState(
    // Child selector
    val selectedChild: UserInfo? = null,
    val userId: String? = null,
    val childrenList: List<UserInfo> = emptyList(),

    // Tabs
    val taskIndex: Int = 0,        // 0 = O'zim, 1 = Farzandim
    val genderIndex: Int = 0,      // tarix ekrani uchun

    // ✅ Filter chip (single-select)
    val activeChip: TaskFilterChip? = null,

    // ✅ Bir nechta list o'rniga aktiv ro'yxat
    val taskList: List<Task> = emptyList(),
    val totalCount: Int = 0,

    // ✅ Pagination
    val hasMore: Boolean = true,
    val offset: Int = 0,
    val pageSize: Int = 20,

    // ✅ Loading holatlari
    val isInitialLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaginating: Boolean = false,
    val isRefiltering: Boolean = false,

    // ✅ Per-item optimistic UI
    val completingIds: Set<String> = emptySet(),
    val deletingIds: Set<String> = emptySet(),

    // Tarix ekrani uchun (CompletedTaskScreen)
    val parentCompletedTaskList: List<Task> = emptyList(),
    val childrenCompletedTaskList: List<Task> = emptyList(),
    val selectedCompletedTaskList: List<Task> = emptyList(),

    // Counters
    val activeTaskCount: Int = 0,
    val allChildrenActiveTaskCount: Int = 0,

    // Response
    val listResponseState: ResponseState<Nothing> = ResponseState.Idle,
    val errorMessage: String? = null,

    // Toggles (boshqa ekranlar uchun)
    val showMineAll: Boolean = false,
    val showChildrenAll: Boolean = false,

    // CompletedTaskScreen uchun
    val completedTaskList: List<Task> = emptyList(),
    val isCompletedLoading: Boolean = false,
    val completedHasMore: Boolean = true,
    val completedOffset: Int = 0,
) {
    val canFetch: Boolean
        get() = taskIndex == 0 || (taskIndex == 1 && selectedChild != null)

    val currentTargetUserId: String?
        get() = if (taskIndex == 0) userId else selectedChild?.userId
}