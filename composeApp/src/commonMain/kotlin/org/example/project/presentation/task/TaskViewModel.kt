@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package org.example.project.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.common.DateTimeUtil
import org.example.project.common.SessionStore
import org.example.project.data.local.AppSettings
import org.example.project.data.mapper.toServerType
import org.example.project.data.mapper.toTask
import org.example.project.data.mapper.toTodoDto
import org.example.project.data.mapper.toUserInfo
import org.example.project.data.remote.model.TodoRequest
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.ChildrenUseCase
import org.example.project.domain.use_case.TodoListUseCase
import org.example.project.domain.use_case.TodoUseCase
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
            }

            TaskEvent.OnConfirmClicked -> {
                requestTodo()
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
               updateTodo(task = event.task)
            }

            is TaskEvent.ShowMineAll -> {
                _state.update {
                    it.copy(
                        showMineAll = !it.showMineAll
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
                    manageTaskList()
                }
            }
        }
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






}
