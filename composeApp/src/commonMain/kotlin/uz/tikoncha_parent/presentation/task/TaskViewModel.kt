@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package uz.tikoncha_parent.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.common.SessionStore
import uz.tikoncha_parent.common.Util.millisToLocalDate
import uz.tikoncha_parent.common.Util.millisToLocalTime
import uz.tikoncha_parent.common.Util.toMillis
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toServerType
import uz.tikoncha_parent.data.mapper.toTask
import uz.tikoncha_parent.data.mapper.toTodoDto
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.data.remote.model.TodoRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.TodoListUseCase
import uz.tikoncha_parent.domain.use_case.TodoUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class TaskViewModel (
    private val todoUseCase: TodoUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val todoListUseCase: TodoListUseCase,

) : ViewModel() {

    private var requestTodoJob: Job? = null
    private var updateTodoJob: Job? = null

    private var childrenJob: Job? = null

    private var listJob: Job? = null


    private val _state = MutableStateFlow(TaskState())
    val state = _state.asStateFlow()


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

            is TaskEvent.OnChildSelected -> {
                _state.update {
                    it.copy(selectedChildren = event.child)
                }
                SessionStore.selectedChildId = event.child.userId
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
                    println("AAAAAA = ${requestTodo()}")
                    requestTodo()
                }
            }

            TaskEvent.GetChildren->{
                loadChildren()
            }

            TaskEvent.OnReset -> {
                _state.update {
                    it.copy(
                        taskSuccess = false,
                        taskLoading = false,
                        taskError = ""
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
                        editingTaskId = event.task.id

                    )
                }
            }
        }
    }

    private fun requestTodo(){
        requestTodoJob?.cancel()
        requestTodoJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    taskLoading = true,
                    taskError = "",
                    taskSuccess = false
                )
            }

            val  selectedChildUserId = SessionStore.selectedChildId

            val request = TodoRequest(
                title = _state.value.title,
                description = _state.value.desc,
                importance = _state.value.importance.toServerType(),
                due_date = DateTimeUtil.formatToIsoString(localDate = _state.value.date, localTime = _state.value.time),
                created_at = DateTimeUtil.getCurrentIsoDateTime(),
                target_user_id = selectedChildUserId,
                id = Uuid.random().toString(),
                is_completed = _state.value.completed
            )

            val result = todoUseCase(request)

            when(result){

                is Resource.Loading -> {}

                is Resource.Error -> {
                    _state.update {
                        println("result = $result.")
                        it.copy(
                            taskSuccess = false,
                            taskError = result.message,
                            taskLoading = false
                        )
                    }
                }

                is Resource.Success -> {

                    val toDo = result.data
                    val allList = state.value.allTaskList.toMutableList()
                    allList.add(toDo)

                    _state.update {
                        it.copy(
                            allTaskList = allList,
                            taskLoading = false,
                            taskError = "",
                            taskSuccess = true
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
                    taskLoading = true,
                    taskError = "",
                    taskSuccess = false
                )
            }


            val request = TodoRequest(
                id = task.id,
                title = task.title,
                description = task.description,
                importance = task.importance.toServerType(),
                due_date = DateTimeUtil.formatToIsoString(millis = task.dateTime),
                created_at = DateTimeUtil.formatToIsoString(millis = task.createdAt),
                target_user_id = task.targetUserId,
                is_completed = task.isCompleted
            )

            val result = todoUseCase(request)

            when(result){

                is Resource.Loading -> {

                }

                is Resource.Error -> {
                    _state.update {
                        println("result = $result.")
                        it.copy(
                            taskSuccess = false,
                            taskError = result.message,
                            taskLoading = false
                        )
                    }
                }

                is Resource.Success -> {
                    loadTasks()

                    val allList = state.value.allTaskList.map { dto->
                        if (dto.id == task.id){
                            task.toTodoDto()
                        }
                        else{
                            dto
                        }
                    }

                    _state.update {
                        it.copy(
                            allTaskList = allList,
                            taskLoading = false,
                            taskError = "",
                            taskSuccess = true
                        )
                    }
                    manageCompletedTaskList()
                    manageTaskList()
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
            targetUserId = SessionStore.selectedChildId ?: "",
            authorId = AppSettings.userId ?: "",
            isMine = true,
        )
    }

    private fun loadChildren() {
        childrenJob?.cancel()
        childrenJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    childrenLoading = true,
                    childrenError = ""
                )
            }

            val response = childrenUseCase.invoke()
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            childrenLoading = false,
                            childrenError = response.message
                        )
                    }
                }

                is Resource.Success -> {

                    val list = response.data.map { it.toUserInfo() }
                    val savedId = SessionStore.selectedChildId
                    val selected = savedId.let { id -> list.firstOrNull() {it.userId == id} }

                    _state.update {
                        it.copy(
                            childrenLoading = false,
                            childrenError = "",
                            childrenList = list,
                            selectedChildren = selected
                        )
                    }

                    if (selected != null){
                        loadTasks()
                    }
                }
            }
        }
    }

    private fun loadTasks(){
        listJob?.cancel()
        listJob = viewModelScope.launch {

            val selectedId = SessionStore.selectedChildId

            if (selectedId == null){
                _state.update {
                    it.copy(
                        listError = "",
                        listLoading = true,
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
                            listLoading = false,
                            listError = result.message,
                            allTaskList = emptyList()
                        )
                    }
                }
                is Resource.Success -> {

                    _state.update {
                        it.copy(
                            listLoading = false,
                            listError = "",
                            allTaskList = result.data
                        )
                    }
                    manageTaskList()
                    manageCompletedTaskList()
                }
            }
        }
    }


    private fun manageTaskList(){
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
}
