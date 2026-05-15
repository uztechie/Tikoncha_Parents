@file:OptIn(ExperimentalTime::class)

package uz.tikoncha_parent.presentation.task.create_task

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.Util.millisToLocalDate
import uz.tikoncha_parent.common.Util.millisToLocalTime
import uz.tikoncha_parent.common.Util.toMillis
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.todo.toImportance
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.chat.GetMyCoinsUseCase
import uz.tikoncha_parent.domain.use_case.todo.CreateTodoParams
import uz.tikoncha_parent.domain.use_case.todo.CreateTodoUseCase
import uz.tikoncha_parent.domain.use_case.todo.UpdateTodoParams
import uz.tikoncha_parent.domain.use_case.todo.UpdateTodoUseCase
import uz.tikoncha_parent.presentation.task.model.ImportanceType
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import kotlin.time.ExperimentalTime

class CreateTaskViewModel(
    private val createTodoUseCase: CreateTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val getMyCoinsUseCase: GetMyCoinsUseCase
) : ScreenModel {

    private var requestJob: Job? = null
    private var updateJob: Job? = null
    private var coinJob: Job? = null

    private val _state = MutableStateFlow(CreateTaskState())
    val state = _state.asStateFlow()

    private val _effect = Channel<CreateTaskEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                selectedChild = AppSettings.selectedChild,
                childrenList = AppSettings.children
            )
        }
        loadParentCoins()
    }

    fun onEvent(event: CreateTaskEvent) {
        when (event) {
            is CreateTaskEvent.OnTitleChange -> _state.update { it.copy(title = event.title) }
            is CreateTaskEvent.OnDescChange -> _state.update { it.copy(desc = event.desc) }
            is CreateTaskEvent.OnDateChange -> _state.update { it.copy(date = event.date) }
            is CreateTaskEvent.OnTimeChange -> _state.update { it.copy(time = event.time) }
            is CreateTaskEvent.OnImportanceChange -> _state.update {
                it.copy(importance = event.importance)
            }
            is CreateTaskEvent.OnChildSelected -> {
                AppSettings.selectedChild = event.child
                _state.update { it.copy(selectedChild = event.child) }
            }
            CreateTaskEvent.LoadParentCoins -> loadParentCoins()

            is CreateTaskEvent.OnChipToggle -> toggleChip(event.value)
            is CreateTaskEvent.OnExtraCoinChange -> setExtraCoin(event.value)

            CreateTaskEvent.OnConfirmClicked -> {
                if (state.value.isEditing && !state.value.editingTaskId.isNullOrEmpty()) {
                    updateTask()
                } else {
                    createTask()
                }
            }
            CreateTaskEvent.OnReset -> resetForm()

            is CreateTaskEvent.OnEditTask -> _state.update {
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
    }

    // ---------------- form helpers ----------------

    private fun toggleChip(value: Int) {
        _state.update { current ->
            val newChips = if (value in current.selectedChips) {
                current.selectedChips - value
            } else {
                // himoya: yangi total balansdan oshmasin
                if (current.totalCoin + value > current.availableCoins) return@update current
                current.selectedChips + value
            }
            // chips kamayganda extraCoin oshib ketsa, tushiramiz
            val maxExtra = (current.availableCoins - newChips.sum()).coerceAtLeast(0)
            current.copy(
                selectedChips = newChips,
                extraCoin = current.extraCoin.coerceAtMost(maxExtra)
            )
        }
    }

    private fun setExtraCoin(value: Int) {
        _state.update { current ->
            val maxExtra = (current.availableCoins - current.selectedChips.sum()).coerceAtLeast(0)
            current.copy(extraCoin = value.coerceIn(0, maxExtra))
        }
    }

    private fun resetForm() {
        _state.update {
            CreateTaskState(
                selectedChild = it.selectedChild,
                childrenList = it.childrenList,
                availableCoins = it.availableCoins
            )
        }
    }

    // ---------------- coins ----------------

    private fun loadParentCoins() {
        coinJob?.cancel()
        coinJob = screenModelScope.launch {
            when (val result = getMyCoinsUseCase()) {
                is Resource.Success -> {
                    _state.update { it.copy(availableCoins = result.data.coins) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(availableCoins = 0) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    // ---------------- create ----------------

    private fun createTask() {
        val s = state.value
        val validationError = validate(s) ?: run {
            requestJob?.cancel()
            requestJob = screenModelScope.launch {
                _state.update { it.copy(taskResponseState = ResponseState.Loading) }

                val params = CreateTodoParams(
                    targetUserId = s.selectedChild?.userId,
                    title = s.title.trim(),
                    description = s.desc.takeIf { it.isNotBlank() },
                    dueAt = toMillis(s.date, s.time),
                    importance = s.importance.toImportance(),
                    coin = s.totalCoin
                )

                createTodoUseCase(params).fold(
                    onSuccess = {
                        _state.update { it.copy(taskResponseState = ResponseState.Idle) }
                        // Server tasdiqlagandan keyin balansni rasmiy qayta yuklash
                        loadParentCoins()
                        sendEffect(CreateTaskEffect.NavigateToSuccess)
                    },
                    onFailure = { e ->
                        _state.update { it.copy(taskResponseState = ResponseState.Idle) }
                        sendEffect(CreateTaskEffect.ShowError(e.message ?: "Xatolik"))
                    }
                )
            }
            return
        }
        sendEffect(CreateTaskEffect.ShowError(validationError))
    }

    // ---------------- update ----------------

    private fun updateTask() {
        val s = state.value
        val id = s.editingTaskId ?: run {
            sendEffect(CreateTaskEffect.ShowError("Vazifa identifikatori topilmadi"))
            return
        }
        val validationError = validate(s) ?: run {
            updateJob?.cancel()
            updateJob = screenModelScope.launch {
                _state.update { it.copy(taskResponseState = ResponseState.Loading) }

                val params = UpdateTodoParams(
                    id = id,
                    title = s.title.trim(),
                    description = s.desc.takeIf { it.isNotBlank() },
                    dueAt = toMillis(s.date, s.time),
                    importance = s.importance.toImportance(),
                    coin = s.totalCoin
                )

                updateTodoUseCase(params).fold(
                    onSuccess = {
                        _state.update { it.copy(taskResponseState = ResponseState.Idle) }
                        loadParentCoins()
                        sendEffect(CreateTaskEffect.NavigateToSuccess)
                    },
                    onFailure = { e ->
                        _state.update { it.copy(taskResponseState = ResponseState.Idle) }
                        sendEffect(CreateTaskEffect.ShowError(e.message ?: "Xatolik"))
                    }
                )
            }
            return
        }
        sendEffect(CreateTaskEffect.ShowError(validationError))
    }

    // ---------------- validation ----------------

    private fun validate(s: CreateTaskState): String? = when {
        s.title.isBlank() -> "Vazifa nomi bo'sh bo'lmasin"
        s.importance == ImportanceType.NONE -> "Muhimlilik darajasini tanlang"
        s.date == null -> "Tugatish sanasini tanlang"
        s.time == null -> "Tugatish vaqtini tanlang"
        s.totalCoin > s.availableCoins -> "Tanga balansingiz yetarli emas"
        !s.isEditing && s.selectedChild == null -> "Farzand tanlanmagan"
        else -> null
    }

    private fun sendEffect(e: CreateTaskEffect) {
        screenModelScope.launch { _effect.send(e) }
    }
}