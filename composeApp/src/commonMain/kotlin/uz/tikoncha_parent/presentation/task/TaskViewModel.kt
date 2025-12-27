@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package uz.tikoncha_parent.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.common.Util.millisToLocalDate
import uz.tikoncha_parent.common.Util.millisToLocalTime
import uz.tikoncha_parent.common.Util.toMillis
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toServerType
import uz.tikoncha_parent.data.mapper.toTask
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.data.remote.model.TodoRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.TodoListUseCase
import uz.tikoncha_parent.domain.use_case.TodoUseCase
import uz.tikoncha_parent.domain.use_case.chat.MyCoinsUseCase
import uz.tikoncha_parent.domain.use_case.chat.UpdateTodoUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class TaskViewModel (
    private val todoUseCase: TodoUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val todoListUseCase: TodoListUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val coinsUseCase: MyCoinsUseCase,
) : ViewModel() {

    private var requestTodoJob: Job? = null
    private var updateTodoJob: Job? = null

    private var childrenJob: Job? = null

    private var listJob: Job? = null


    private val _state = MutableStateFlow(TaskState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
                childrenList = AppSettings.children
            )
        }
        loadTasks()
        onEvent(TaskEvent.LoadParentCoins)
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.OnDateChange -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is TaskEvent.OnTimeChange -> {
                _state.update {
                    it.copy(
                        time = event.time
                    )
                }
            }

            is TaskEvent.OnDescChange -> {
                _state.update {
                    it.copy(
                        desc = event.desc
                    )
                }
            }

            is TaskEvent.OnImportanceChange -> {
                _state.update {
                    it.copy(
                        importance = event.importance
                    )
                }
            }

            is TaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is TaskEvent.OnCoinChange -> {
                _state.update {
                    it.copy(
                        coin = event.coin
                    )
                }
            }

            TaskEvent.LoadParentCoins -> {
                viewModelScope.launch {
                    val request = coinsUseCase()
                    when(request){
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    availableCoins = request.data.coins
                                )
                            }
                        }
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    availableCoins = 0
                                )
                            }
                        }
                        is Resource.Loading -> { }
                    }
                }
            }

            is TaskEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChild = event.child)
                }
                AppSettings.selectedChild = event.child
                loadTasks()
            }

            is TaskEvent.OnGenderSelected -> {
                _state.update {
                    it.copy(
                        genderIndex = event.genderIndex
                    )
                }
                recomputeSelectedCompleted()
            }

            TaskEvent.OnConfirmClicked -> {
                val editingId = state.value.editingTaskId
                if (state.value.isEditing && !editingId.isNullOrEmpty()){
                    val editedTask = buildEditedTaskFromState()
                    updateTodo(editedTask)
                }
                else {
                    requestTodo()
                }
            }

            TaskEvent.OnReset -> {
                _state.update {
                    it.copy(
                        taskResponseState = ResponseState.Idle,
                        editingTaskId = null,
                        editingTaskCreatedAt = null
                    )
                }
            }

            TaskEvent.LoadTasks -> {
                loadTasks()
            }

            is TaskEvent.OnCompletedTask -> {
               updateTodo(task = event.task.copy(isCompleted = true))
            }

            is TaskEvent.ShowMineAll -> {
                _state.update {
                    it.copy(
                        showMineAll = !it.showMineAll
                    )
                }
            }

            is TaskEvent.ShowChildrenAll -> {
                _state.update {
                    it.copy(
                        showChildrenAll = !it.showChildrenAll
                    )
                }
            }

            is TaskEvent.OnEditTask -> {
                _state.update {
                    it.copy(
                        title = event.task.title,
                        desc = event.task.description,
                        date = millisToLocalDate(event.task.dateTime),
                        time = millisToLocalTime(event.task.dateTime),
                        importance = event.task.importance,
                        completed = event.task.isCompleted,
                        isEditing = true,
                        editingTaskId = event.task.id,
                        editingTaskCreatedAt = event.task.createdAt
                    )
                }
            }

            TaskEvent.LoadAllChildrenActiveTasks -> {
                loadAllChildrenActiveTasks()
            }
        }
    }

    private fun requestTodo(){
        requestTodoJob?.cancel()
        requestTodoJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    taskResponseState = ResponseState.Loading,
                )
            }

            val  selectedChildUserId = state.value.selectedChild?.userId

            val request = TodoRequest(
                title = _state.value.title,
                description = _state.value.desc,
                importance = _state.value.importance.toServerType(),
                due_date = DateTimeUtil.formatToIsoString(localDate = _state.value.date, localTime = _state.value.time, timeZone = TimeZone.UTC),
                created_at = DateTimeUtil.getCurrentIsoDateTime(timeZone = TimeZone.UTC),
                target_user_id = selectedChildUserId,
                id = Uuid.random().toString(),
                is_completed = _state.value.completed,
                coin = _state.value.coin
            )

            val result = todoUseCase(request)

            when(result){

                is Resource.Loading -> {}

                is Resource.Error -> {
                    _state.update {
                        println("result = $result.")
                        it.copy(
                            taskResponseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
                            )
                        )
                    }
                }

                is Resource.Success -> {

                    val gift = state.value.coin
                    val childId = selectedChildUserId.orEmpty()
                    val availableCoins = state.value.availableCoins

                    if (gift > 0 && gift <= availableCoins && childId.isNotEmpty()){
                        val giftCoins = coinsUseCase()
                        when(giftCoins){
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        availableCoins = (it.availableCoins - gift)
                                    )
                                }
                            }
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                        }
                    }

                    val toDo = result.data
                    val allList = state.value.allTaskList.toMutableList()
                    allList.add(toDo)

                    _state.update {
                        it.copy(
                            allTaskList = allList,
                            taskResponseState = ResponseState.Success()
                        )
                    }
                    manageCompletedTaskList()
                    manageTaskList()
                }
            }
        }
    }

    private fun updateTodo(task: Task){
        updateTodoJob?.cancel()
        updateTodoJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    taskResponseState = ResponseState.Loading
                )
            }


            val request = TodoRequest(
                id = task.id,
                title = task.title,
                description = task.description,
                importance = task.importance.toServerType(),
                due_date = DateTimeUtil.formatToIsoString(millis = task.dateTime, timeZone = TimeZone.UTC),
                created_at = DateTimeUtil.formatToIsoString(millis = task.createdAt, timeZone = TimeZone.UTC),
                target_user_id = task.targetUserId,
                is_completed = task.isCompleted
            )

            val result = updateTodoUseCase(request)

            when(result){

                is Resource.Loading -> {

                }

                is Resource.Error -> {
                    _state.update {
                        println("result = $result.")
                        it.copy(
                            taskResponseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            taskResponseState = ResponseState.Success()
                        )
                    }

                    loadTasks()
                }
            }
        }
    }

    private fun buildEditedTaskFromState(): Task {
        val state = this@TaskViewModel.state.value

        val dueMillis = toMillis(state.date, state.time)
        val editedNowMillis = Clock.System.now().toEpochMilliseconds()

        return Task(
            id = state.editingTaskId ?: "",
            title = state.title,
            description = state.desc,
            importance = state.importance,
            isCompleted = state.completed == true,
            dateTime = dueMillis,
            createdAt = editedNowMillis,
            targetUserId = state.selectedChild?.userId ?: "",
            authorId = AppSettings.userId ?: "",
            isMine = true,
        )
    }


    private fun loadTasks(){
        listJob?.cancel()
        listJob = viewModelScope.launch {

            val selectedId = state.value.selectedChild?.userId

            if (selectedId == null){
                _state.update {
                    it.copy(
                        listResponseState = ResponseState.Loading,
                        allTaskList = emptyList()
                    )
                }
                return@launch
            }

            val result = todoListUseCase.invoke(selectedId)

            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                           listResponseState = ResponseState.Error(
                               res = result.resId,
                               message = result.message
                           ),
                            allTaskList = emptyList()
                        )
                    }
                }
                is Resource.Success -> {

                    _state.update {
                        it.copy(
                            listResponseState = ResponseState.Success(),
                            allTaskList = result.data
                        )
                    }
                    manageTaskList()
                    manageCompletedTaskList()
                }
            }
        }
    }


    fun manageTaskList(){
        val allList = state.value.allTaskList

        _state.update {
            it.copy(
                childrenTaskList = allList.filter { it.author_id != AppSettings.userId && !it.is_completed }.map { it.toTask() }.sortedBy { it.importance == ImportanceType.MOST_IMPORTANT },
                parentTaskList = allList.filter { it.author_id == AppSettings.userId && !it.is_completed}.map { it.toTask() }.sortedBy { it.importance == ImportanceType.MOST_IMPORTANT },
            )
        }
    }

    private fun manageCompletedTaskList(){
        val allList = state.value.allTaskList

        _state.update {
            it.copy(
                childrenCompletedTaskList = allList.filter { it.author_id != AppSettings.userId && it.is_completed }.map { it.toTask() }.sortedBy { it.importance == ImportanceType.MOST_IMPORTANT },
                parentCompletedTaskList = allList.filter { it.author_id == AppSettings.userId && it.is_completed}.map { it.toTask() }.sortedBy { it.importance == ImportanceType.MOST_IMPORTANT },
            )
        }
        recomputeSelectedCompleted()
    }

    private fun recomputeSelectedCompleted(){
        val gender = state.value.genderIndex
        val selected = if (gender == 0)
            state.value.childrenCompletedTaskList
        else
            state.value.parentCompletedTaskList

        _state.update {
            it.copy(
                selectedCompletedTaskList = selected
            )
        }
    }

    private fun loadAllChildrenActiveTasks(){
        viewModelScope.launch {
            val childrenRes = childrenUseCase.invoke()
            when(childrenRes) {
                is Resource.Success -> {
                    val children = childrenRes.data.map { it.toUserInfo() }
                    var total = 0

                    for (child in children) {
                        val todosRes = todoListUseCase.invoke(child.userId)
                        when (todosRes) {
                            is Resource.Success -> {
                                total += todosRes.data.count { !it.is_completed }
                            }

                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                        }
                    }
                    _state.update {
                        it.copy(
                            allChildrenActiveTaskCount = total
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            allChildrenActiveTaskCount = 0
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}
