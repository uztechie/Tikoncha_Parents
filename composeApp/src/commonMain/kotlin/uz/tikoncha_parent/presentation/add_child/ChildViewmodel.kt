package uz.tikoncha_parent.presentation.add_child

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.Util.normalizePhone
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.AddChildRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.AddChildUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ChildViewmodel(
    private val addChildUseCase: AddChildUseCase,
    private val childrenUseCase: ChildrenUseCase
) : ScreenModel {

    private var addChildJob: Job? = null
    private var watchJob: Job? = null

    // addChild chaqirilishidan oldingi farzandlar soni va raqamlari
    private var previousChildrenPhone: Set<String> = emptySet()
    private var previousChildrenCount: Int = 0

    private val _state = MutableStateFlow(ChildState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                showConnectChildTutorialCard = AppSettings.showBindChildTutorial
            )
        }
    }

    fun onEvent(event: ChildEvent) {
        when (event) {
            is ChildEvent.OnNumberInsert -> {
                _state.update {
                    it.copy(
                        number = event.number,
                        fullNumber = "+998${event.number}"
                    )
                }
            }


            ChildEvent.OnAddClicked -> {
                addChild()
            }

            ChildEvent.Reset -> {
                _state.update {
                    it.copy(
                        responseState = ResponseState.Idle,
                    )
                }
            }

            ChildEvent.Clear -> {
                _state.update {
                    it.copy(
                        number = ""
                    )
                }
            }

            ChildEvent.OnSuccessDismissed -> {
                watchJob?.cancel()
                previousChildrenPhone = emptySet()
                previousChildrenCount = 0
                _state.update { ChildState() }
            }

            ChildEvent.StartWatching -> {
                startWatching()
            }

            ChildEvent.StopWatching -> {
                watchJob?.cancel()
            }
        }
    }

    private fun startWatching() {
        if (state.value.confirmCode.isEmpty() || state.value.childJoined) return

        watchJob?.cancel()
        watchJob = screenModelScope.launch {
            val targetPhone = normalizePhone(state.value.fullNumber)
            val maxAttempts = 100
            var attempts = 0
            var consecutiveError = 0

            while (isActive && attempts < maxAttempts) {
                val snapShot = fetchChildrenSnapshot()
                if (snapShot.success) {
                    consecutiveError = 0
                    val phoneMatched =
                        targetPhone in snapShot.phones && targetPhone !in previousChildrenPhone
                    val countIncreased = snapShot.count > previousChildrenCount

                    if (phoneMatched || countIncreased) {
                        _state.update {
                            it.copy(
                                childJoined = true
                            )
                        }
                        return@launch
                    }
                    attempts++
                    delay(10_000)
                } else {
                    consecutiveError++
                    val backoffMs = when {
                        consecutiveError >= 5 -> 15_000L
                        consecutiveError >= 3 -> 10_000L
                        else -> 5_000L
                    }
                    attempts++
                    delay(backoffMs)
                }
            }
        }
    }

    private suspend fun fetchChildrenSnapshot(): ChildrenSnapshot {
        return when (val result = childrenUseCase()) {
            is Resource.Success -> {
                val list = result.data
                ChildrenSnapshot(
                    success = true,
                    count = list.size,
                    phones = list.mapNotNull { user ->
                        runCatching { normalizePhone(user.phone_number) }.getOrNull()
                    }.toSet()
                )
            }

            is Resource.Error -> {
                ChildrenSnapshot(
                    success = false
                )
            }

            is Resource.Loading -> {
                ChildrenSnapshot(
                    success = false
                )
            }
        }
    }

    private fun addChild() {

        _state.update {
            it.copy(
                responseState = ResponseState.Idle,
            )
        }
        _state.update {
            it.copy(
                responseState = ResponseState.Loading,
            )
        }

        addChildJob?.cancel()
        addChildJob = screenModelScope.launch {

            val snapshot = fetchChildrenSnapshot()
            previousChildrenPhone = snapshot.phones
            previousChildrenCount = snapshot.count

            val request = AddChildRequest(
                phone_number = state.value.fullNumber
            )

            val response = addChildUseCase(request)
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Error(
                                res = response.resId,
                                message = response.message
                            ),
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            confirmCode = response.data,
                            responseState = ResponseState.Success(),
                        )
                    }
                }
            }
        }
    }
}