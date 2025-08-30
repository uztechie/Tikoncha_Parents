@file:OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)

package org.example.project.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.data.local.AppSettings
import org.example.project.data.mapper.toImportanceType
import org.example.project.data.mapper.toServerType
import org.example.project.data.mapper.toTask
import org.example.project.data.mapper.toUserInfo
import org.example.project.data.remote.model.TodoDto
import org.example.project.data.remote.model.TodoRequest
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.ChildrenUseCase
import org.example.project.domain.use_case.TodoListUseCase
import org.example.project.domain.use_case.TodoUseCase
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TaskViewModel (
    private val todoUseCase: TodoUseCase,
    private val childrenUseCase: ChildrenUseCase,
    private val todoListUseCase: TodoListUseCase,

) : ViewModel() {

    private var requestTodoJob: Job? = null

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
                AppSettings.selectedChildId = event.child.userId
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
            val request = TodoRequest(
                title = _state.value.title,
                description = _state.value.desc,
                importance = _state.value.importance.toServerType(),
                due_date = formatToIsoString(localDate = _state.value.date, localTime = _state.value.time),
                created_at = getCurrentIsoDateTime(),
                target_user_id = AppSettings.userId,
                id = Uuid.random().toString()
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
                    val savedId = AppSettings.selectedChildId
                    val selected = list.firstOrNull{ it.userId == savedId} ?: list.firstOrNull()

                    _state.update {
                        it.copy(
                            childrenLoading = false,
                            childrenError = "",
                            childrenList = response.data.map { userInfoDto -> userInfoDto.toUserInfo() }
                        )
                    }
                }
            }
        }
    }

    private fun loadTasks(){
        listJob?.cancel()
        listJob =   viewModelScope.launch {
            _state.update {
                it.copy(
                    listError = "",
                    listLoading = true,
                    allTaskList = emptyList()
                )
            }


            val result = todoListUseCase.invoke(AppSettings.selectedChildId)

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
                childrenTaskList = allList.filter { it.author_id == null }.map { it.toTask() }.sortedBy { it.importance == ImportanceType.MOST_IMPORTANT },
                parentTaskList = allList.filter { it.author_id != null }.map { it.toTask() }.sortedBy { it.importance == ImportanceType.MOST_IMPORTANT },
            )
        }
    }

    fun getCurrentIsoDateTime(): String {
        val nowInstant = Clock.System.now()
        val localDateTime = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())

        val year = localDateTime.year.toString().padStart(4, '0')
        val month = localDateTime.month.ordinal.toString().padStart(2, '0')
        val day = localDateTime.day.toString().padStart(2, '0')
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')
        val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }







    fun formatToIsoString(
        localDate: LocalDate?,
        localTime: LocalTime?,
        timeZone: TimeZone = TimeZone.UTC
    ): String {
        if (localTime == null || localDate == null){
            return ""
        }
        // Combine LocalDate + LocalTime into LocalDateTime
        val localDateTime = LocalDateTime(localDate, localTime)

        val year = localDateTime.year.toString().padStart(4, '0')
        val month = localDateTime.month.ordinal.toString().padStart(2, '0')
        val day = localDateTime.day.toString().padStart(2, '0')
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        val second = localDateTime.second.toString().padStart(2, '0')
        val millis = (localDateTime.nanosecond / 1_000_000).toString().padStart(3, '0')

        return "$year-$month-${day}T$hour:$minute:$second.${millis}"
    }
}
