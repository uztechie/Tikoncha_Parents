package uz.tikoncha_parent.presentation.task.completedTask

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
import uz.tikoncha_parent.domain.model.todo.CreatedByRole
import uz.tikoncha_parent.domain.model.todo.TodoFilter
import uz.tikoncha_parent.domain.model.todo.TodosQuery
import uz.tikoncha_parent.domain.use_case.todo.GetTodosUseCase

class CompletedTaskViewModel(
    private val getTodosUseCase: GetTodosUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(CompletedTaskState())
    val state = _state.asStateFlow()

    private val _effect = Channel<CompletedTaskEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
                userId = AppSettings.userId
            )
        }
    }

    fun onEvent(event: CompletedTaskEvent){
        when(event){
            CompletedTaskEvent.LoadTasks -> initialLoad()
            CompletedTaskEvent.OnRefresh -> refresh()
            CompletedTaskEvent.OnLoadMore -> loadMore()
            CompletedTaskEvent.ClearError -> _state.update { it.copy(errorMessage = null) }
            is CompletedTaskEvent.OnTabSelected -> changeTab(event.taskIndex)
        }
    }


    private fun initialLoad(){
        val s = state.value
        if(!s.canFetch){
            _state.update {
                it.copy(
                    taskList = emptyList(),
                    totalCount = 0,
                    offset = 0,
                    hasMore = true,
                    isInitialLoading = false,
                    isRefreshing = false,
                    isPaginating = false,
                )
            }
            return
        }

        val hasData = s.taskList.isNotEmpty()
        _state.update {
            it.copy(
                isInitialLoading = !hasData,
                isRefiltering = hasData
            )
        }
        firstPage()
    }

    private fun changeTab(index: Int){
        if (index == state.value.taskIndex) return

        loadJob?.cancel()
        val willFetch = state.value.canFetch

        _state.update {
            it.copy(
                taskIndex = index,
                errorMessage = null,
                taskList = emptyList(),
                totalCount = 0,
                offset = 0,
                hasMore = true,
                isInitialLoading = willFetch,
                isRefiltering = false,
                isPaginating = false
            )
        }

        if (willFetch){
            firstPage()
        }
    }

    private fun firstPage(){
        if (!state.value.canFetch){
            _state.update {
                it.copy(
                    taskList = emptyList(),
                    totalCount = 0,
                    offset = 0,
                    hasMore = true,
                    isInitialLoading = false,
                    isRefiltering = false,
                    isPaginating = false
                )
            }
            return
        }

        loadJob?.cancel()
        _state.update {
            it.copy(
                errorMessage = null,
                offset = 0,
                hasMore = true,
                isPaginating = false
            )
        }

        loadJob = screenModelScope.launch { fetchPage(reset = true) }
    }

    private fun refresh(){
        if (!state.value.canFetch) return
        loadJob?.cancel()
        _state.update {
            it.copy(
                isRefreshing = true,
                errorMessage = null,
                offset = 0,
                hasMore = true,
                isPaginating = false
            )
        }
        loadJob = screenModelScope.launch { fetchPage(reset = true) }
    }

    private fun loadMore(){
        val s = state.value
        if (s.isPaginating || s.isInitialLoading || s.isRefreshing || s.isRefiltering) return

        _state.update {
            it.copy(
                isPaginating = true
            )
        }

        loadJob = screenModelScope.launch {
            fetchPage(reset = false)
        }
    }

    private suspend fun fetchPage(reset: Boolean){
        val s = state.value
        val role = if (s.taskIndex == 0) CreatedByRole.PARENT else CreatedByRole.CHILD

        val query = TodosQuery(
            targetUserId = s.selectedChild?.userId,
            filter = TodoFilter(
                isCompleted = true,
                createdByRole = role
            ),
            limit = s.pageSize,
            offset = if (reset) 0 else s.offset
        )

        getTodosUseCase.invoke(query).fold(
            onSuccess = { page ->
                val mapped = page.items.map { it.toTask() }
                _state.update { current->
                    val newList = if (reset) mapped else current.taskList + mapped
                    current.copy(
                        taskList = newList,
                        totalCount = page.total,
                        offset = newList.size,
                        hasMore = page.hasMore,
                        isInitialLoading = false,
                        isRefiltering = false,
                        isRefreshing = false,
                        isPaginating = false,
                        errorMessage = null
                    )
                }
            },
            onFailure = { e->
                _state.update { current ->
                    current.copy(
                        isInitialLoading = false,
                        isRefiltering = false,
                        isRefreshing = false,
                        isPaginating = false,
                        errorMessage = if (reset) e.message ?: "Xatolik" else current.errorMessage
                    )
                }
                if (!reset) sendEffect(CompletedTaskEffect.ShowError(e.message ?: "Xatolik"))
            }
        )



    }


    private fun sendEffect(effect: CompletedTaskEffect) {
        screenModelScope.launch { _effect.send(effect) }
    }

}