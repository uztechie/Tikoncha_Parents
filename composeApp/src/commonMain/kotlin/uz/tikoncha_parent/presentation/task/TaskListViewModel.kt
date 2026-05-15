package uz.tikoncha_parent.presentation.task

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.todo.toTask
import uz.tikoncha_parent.domain.model.todo.TodoFilter
import uz.tikoncha_parent.domain.model.todo.TodosQuery
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.todo.CompleteTodoUseCase
import uz.tikoncha_parent.domain.use_case.todo.DeleteTodoUseCase
import uz.tikoncha_parent.domain.use_case.todo.GetTodosUseCase
import uz.tikoncha_parent.presentation.task.model.Task
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class TaskListViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val completeTodoUseCase: CompleteTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val childrenUseCase: ChildrenUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(TaskListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<TaskListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
                userId = AppSettings.userId,
                childrenList = AppSettings.children
            )
        }
    }

    fun onEvent(event: TaskListEvent) {
        when (event) {
            TaskListEvent.LoadTasks -> firstPage()
            TaskListEvent.OnRefresh -> refresh()
            TaskListEvent.OnLoadMore -> loadMore()
            TaskListEvent.OnRetry -> firstPage()
            TaskListEvent.LoadAllChildrenActiveTasks -> loadAllChildrenActiveTasks()

            is TaskListEvent.OnFilterChipToggled -> toggleChip(event.chip)

            is TaskListEvent.OnTaskSelected -> changeTab(event.taskIndex)
            is TaskListEvent.OnChildSelected -> selectChild(event.child)
            is TaskListEvent.OnGenderSelected -> {
                _state.update {
                    it.copy(
                        genderIndex = event.genderIndex,
                        completedTaskList = emptyList(),
                        completedOffset = 0,
                        completedHasMore = true
                    )
                }
                loadCompletedTasks(reset = true)
            }

            is TaskListEvent.OnCompletedTask -> completeTask(event.task)
            is TaskListEvent.OnDeleteTask -> deleteTask(event.task)

            TaskListEvent.ShowMineAll -> _state.update { it.copy(showMineAll = !it.showMineAll) }
            TaskListEvent.ShowChildrenAll -> _state.update { it.copy(showChildrenAll = !it.showChildrenAll) }
            TaskListEvent.LoadCompletedTasks -> loadCompletedTasks(reset = true)
            TaskListEvent.LoadMoreCompleted -> loadCompletedTasks(reset = false)
        }
    }

    private fun loadCompletedTasks(reset: Boolean) {
        val s = state.value
        if (s.isCompletedLoading) return
        if (!reset && !s.completedHasMore) return

        screenModelScope.launch {
            _state.update { it.copy(isCompletedLoading = true) }

            val targetId = if (s.genderIndex == 0) s.selectedChild?.userId else null

            val query = TodosQuery(
                targetUserId = targetId,
                filter = buildFilter(null, forHistory = true),  // isCompleted=true
                limit = s.pageSize,
                offset = if (reset) 0 else s.completedOffset
            )

            getTodosUseCase(query).fold(
                onSuccess = { page ->
                    val mapped = page.items.map { it.toTask() }
                    _state.update { current ->
                        val newList = if (reset) mapped else current.completedTaskList + mapped
                        current.copy(
                            completedTaskList = newList,
                            completedOffset = newList.size,
                            completedHasMore = page.hasMore,
                            isCompletedLoading = false
                        )
                    }
                },
                onFailure = { e ->
                    _state.update { it.copy(isCompletedLoading = false) }
                    sendEffect(TaskListEffect.ShowError(e.message ?: "Yuklanmadi"))
                }
            )
        }
    }

    // ---------------- tab / child / chip ----------------

    private fun changeTab(index: Int) {
        if (index == state.value.taskIndex) return
        val willFetch = index == 0 || state.value.selectedChild != null
        _state.update {
            it.copy(
                taskIndex = index,
                taskList = emptyList(),
                offset = 0,
                hasMore = true,
                errorMessage = null,
                isInitialLoading = willFetch,
                listResponseState = if (willFetch) ResponseState.Loading else ResponseState.Idle
            )
        }
        if (willFetch) firstPage()
    }

    private fun selectChild(child: uz.tikoncha_parent.domain.model.UserInfo) {
        if (child.userId == state.value.selectedChild?.userId) return
        AppSettings.selectedChild = child
        _state.update {
            it.copy(
                selectedChild = child,
                taskList = emptyList(),
                offset = 0,
                hasMore = true,
                errorMessage = null,
                isInitialLoading = true,
                listResponseState = ResponseState.Loading
            )
        }
        if (state.value.canFetch) firstPage() else _state.update { it.copy(isInitialLoading = false) }
    }

    private fun toggleChip(chip: TaskFilterChip) {
        val hasExistingData = state.value.taskList.isNotEmpty()
        _state.update {
            val new = if (it.activeChip == chip) null else chip
            it.copy(
                activeChip = new,
                offset = 0,
                hasMore = true,
                errorMessage = null,
                isInitialLoading = !hasExistingData,
                isRefiltering = hasExistingData,
                listResponseState = ResponseState.Loading
            )
        }
        firstPage()
    }

    // ---------------- fetch ----------------

    private fun firstPage() {
        if (!state.value.canFetch) {
            _state.update { it.copy(taskList = emptyList(), totalCount = 0) }
            return
        }
        loadJob?.cancel()
        _state.update {
            it.copy(
                isInitialLoading = true,
                listResponseState = ResponseState.Loading,
                errorMessage = null,
                taskList = emptyList(),
                offset = 0,
                hasMore = true
            )
        }
        loadJob = screenModelScope.launch { fetchPage(reset = true) }
    }

    private fun refresh() {
        if (!state.value.canFetch) return
        loadJob?.cancel()
        _state.update { it.copy(isRefreshing = true, errorMessage = null) }
        loadJob = screenModelScope.launch { fetchPage(reset = true) }
    }

    private fun loadMore() {
        val s = state.value
        if (s.isPaginating || s.isInitialLoading || s.isRefreshing) return
        if (!s.hasMore || !s.canFetch) return
        _state.update { it.copy(isPaginating = true) }
        screenModelScope.launch { fetchPage(reset = false) }
    }

    private suspend fun fetchPage(reset: Boolean) {
        val s = state.value
        val query = TodosQuery(
            targetUserId = s.currentTargetUserId,
            filter = buildFilter(s.activeChip, forHistory = false),
            limit = s.pageSize,
            offset = if (reset) 0 else s.offset
        )

        getTodosUseCase(query).fold(
            onSuccess = { page ->
                val mapped = page.items.map { it.toTask() }
                _state.update { current ->
                    val newList = if (reset) mapped else current.taskList + mapped
                    current.copy(
                        taskList = newList,
                        totalCount = page.total,
                        offset = newList.size,
                        hasMore = page.hasMore,
                        isInitialLoading = false,
                        isRefreshing = false,
                        isPaginating = false,
                        listResponseState = ResponseState.Idle,
                        errorMessage = null,
                        // ✅ activeTaskCount faqat filter yo'q va "O'zim" tabida yangilanadi
                        activeTaskCount = if (current.activeChip == null && current.taskIndex == 0) {
                            page.total
                        } else {
                            current.activeTaskCount
                        }
                    )
                }
            },
            onFailure = { e ->
                _state.update { current ->
                    current.copy(
                        isInitialLoading = false,
                        isRefreshing = false,
                        isPaginating = false,
                        listResponseState = ResponseState.Idle,
                        errorMessage = if (reset) e.message ?: "Xatolik" else current.errorMessage
                    )
                }
                if (!reset) sendEffect(TaskListEffect.ShowError(e.message ?: "Xatolik"))
            }
        )
    }

    // ---------------- complete (Tekshirildi) ----------------

    private fun completeTask(task: Task) {
        // Server invariantiga: faqat is_child_done=true bo'lganda chaqirish mumkin.
        // UI tugmasi disable bo'lishi kerak, ammo himoya sifatida ham tekshiramiz.
        if (!task.isChildDone || task.isCompleted) return
        if (task.id in state.value.completingIds) return

        _state.update { it.copy(completingIds = it.completingIds + task.id) }

        screenModelScope.launch {
            completeTodoUseCase(task.id).fold(
                onSuccess = {
                    // Verified bo'ldi → asosiy listdan olib tashlaymiz (tarixga ketdi)
                    _state.update {
                        it.copy(
                            taskList = it.taskList.filterNot { t -> t.id == task.id },
                            totalCount = (it.totalCount - 1).coerceAtLeast(0),
                            completingIds = it.completingIds - task.id
                        )
                    }
                    sendEffect(TaskListEffect.TaskMarkedAsCompleted)
                },
                onFailure = { e ->
                    _state.update { it.copy(completingIds = it.completingIds - task.id) }
                    sendEffect(TaskListEffect.ShowError(e.message ?: "Tekshirishda xatolik"))
                }
            )
        }
    }

    // ---------------- delete ----------------

    private fun deleteTask(task: Task) {
        if (task.id in state.value.deletingIds) return
        _state.update { it.copy(deletingIds = it.deletingIds + task.id) }

        screenModelScope.launch {
            deleteTodoUseCase(task.id).fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            taskList = it.taskList.filterNot { t -> t.id == task.id },
                            totalCount = (it.totalCount - 1).coerceAtLeast(0),
                            deletingIds = it.deletingIds - task.id
                        )
                    }
                    sendEffect(TaskListEffect.TaskDeleted)
                },
                onFailure = { e ->
                    _state.update { it.copy(deletingIds = it.deletingIds - task.id) }
                    sendEffect(TaskListEffect.ShowError(e.message ?: "O'chirishda xatolik"))
                }
            )
        }
    }

    // ---------------- counter ----------------

    private fun loadAllChildrenActiveTasks() {
        val children = state.value.childrenList
        if (children.isEmpty()) {
            _state.update { it.copy(allChildrenActiveTaskCount = 0) }
            return
        }
        screenModelScope.launch {
            var total = 0
            for (child in children) {
                val q = TodosQuery(
                    targetUserId = child.userId,
                    filter = TodoFilter(isCompleted = false),
                    limit = 1,
                    offset = 0
                )
                getTodosUseCase(q).onSuccess { page -> total += page.total }
            }
            _state.update { it.copy(allChildrenActiveTaskCount = total) }
        }
    }

    // ---------------- helpers ----------------

    private fun buildFilter(chip: TaskFilterChip?, forHistory: Boolean): TodoFilter {
        if (forHistory) return TodoFilter(isCompleted = true)
        return when (chip) {
            TaskFilterChip.IN_PROGRESS -> TodoFilter(
                isCompleted = false, isChildDone = false, isExpired = false
            )
            TaskFilterChip.DONE_BY_CHILD -> TodoFilter(
                isCompleted = false, isChildDone = true, isExpired = null
            )
            TaskFilterChip.OVERDUE -> TodoFilter(
                isCompleted = false, isChildDone = false, isExpired = true
            )
            null -> TodoFilter(isCompleted = false)
        }
    }

    private fun sendEffect(effect: TaskListEffect) {
        screenModelScope.launch { _effect.send(effect) }
    }
}