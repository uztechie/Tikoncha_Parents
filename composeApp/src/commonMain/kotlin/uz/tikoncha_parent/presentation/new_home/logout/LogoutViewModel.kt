package uz.tikoncha_parent.presentation.new_home.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.LogoutUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class LogoutViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LogoutState())
    val state = _state.asStateFlow()

    private var logoutJob: Job? = null

    fun onEvent(event: LogoutEvent) {
        when (event) {
            is LogoutEvent.CreateRequest -> {

            }

            is LogoutEvent.DeleteRequest -> {

            }

            LogoutEvent.RefreshList -> {

            }

            is LogoutEvent.SetType -> {
                _state.update {
                    it.copy(
                        currentType = event.type
                    )
                }
            }

            LogoutEvent.ResetResponseState -> {
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

    fun loadLogout(){
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    listResponseState = ResponseState.Loading,
                )
            }

            val result = logoutUseCase()
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