package uz.tikoncha_parent.presentation.new_home.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.ParentRequestsUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ParentRequestViewModel(
    private val parentRequestsUseCase: ParentRequestsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ParentRequestState())
    val state = _state.asStateFlow()

    private var ParentRequestJob: Job? = null

    fun onEvent(event: ParentRequestEvent) {
        when (event) {
            is ParentRequestEvent.CreateRequest -> {

            }

            is ParentRequestEvent.DeleteRequest -> {

            }

            ParentRequestEvent.RefreshList -> {

            }

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
        }
    }

    fun loadParentRequests(){
        ParentRequestJob?.cancel()
        ParentRequestJob = viewModelScope.launch {
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
}