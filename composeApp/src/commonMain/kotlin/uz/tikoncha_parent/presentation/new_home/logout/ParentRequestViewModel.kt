package uz.tikoncha_parent.presentation.new_home.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ParentRequestsUseCase
import uz.tikoncha_parent.domain.use_case.UpdateParentRequestStatusUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ParentRequestViewModel(
    private val parentRequestsUseCase: ParentRequestsUseCase,
    private val updateResponseUseCase: UpdateParentRequestStatusUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(ParentRequestState())
    val state = _state.asStateFlow()

    private var parentRequestJob: Job? = null
    private var updateRequestJob: Job? = null


    fun onEvent(event: ParentRequestEvent) {
        when (event) {

            is ParentRequestEvent.SetType -> {
                _state.update {
                    it.copy(
                        currentType = event.type
                    )
                }
            }

            ParentRequestEvent.ResetResponseState -> {
                _state.update {
                    it.copy(
                        listResponseState = ResponseState.Idle,
                        createResponseState = ResponseState.Idle,
                        deleteResponseState = ResponseState.Idle
                    )
                }
            }

            ParentRequestEvent.AccessSelectedRequest -> {
                updateParentRequest(status = "access", isSelect = true)
            }
            ParentRequestEvent.DenySelectedRequest -> {
                updateParentRequest(status = "deny", isSelect = false)
            }
            is ParentRequestEvent.SelectedRequest -> {
                _state.update {
                    it.copy(
                        selectedRequest = event.request
                    )
                }
            }
        }
    }

    fun loadParentRequests(){
        parentRequestJob?.cancel()
        parentRequestJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    listResponseState = ResponseState.Loading,
                )
            }

            val result = parentRequestsUseCase()
            when(result){
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            listResponseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    val list = result.data
                    _state.update {
                        it.copy(
                            listResponseState = ResponseState.Success(),
                            items = list?: emptyList()
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun updateParentRequest(status: String,isSelect: Boolean){
        val selected = _state.value.selectedRequest
        updateRequestJob?.cancel()
        updateRequestJob = screenModelScope.launch {
            if (isSelect){
                _state.update {
                    it.copy(
                        createResponseState = ResponseState.Loading
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        deleteResponseState = ResponseState.Loading
                    )
                }
            }

            val result = updateResponseUseCase(
                requestId = selected?.requestId ?: "",
                status = status
            )

            when(result){
                is Resource.Error -> {
                    if (isSelect){
                        _state.update {
                            it.copy(
                                createResponseState = ResponseState.Error(
                                    res = result.resId,
                                    message = result.message
                                )
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                deleteResponseState = ResponseState.Error(
                                    res = result.resId,
                                    message = result.message
                                )
                            )
                        }
                    }
                }
                is Resource.Success -> {
                    val updateItem = result.data
                    _state.update { state ->
                        val newsList = state.items.map { item->
                            if (item.requestId == updateItem.requestId){
                                updateItem
                            } else {
                                item
                            }
                        }
                        if (isSelect){
                            state.copy(
                                createResponseState = ResponseState.Success(),
                                items = newsList,
                                selectedRequest = null
                            )
                        } else {
                            state.copy(
                                deleteResponseState = ResponseState.Success(),
                                items = newsList,
                                selectedRequest = null
                            )
                        }
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }
}