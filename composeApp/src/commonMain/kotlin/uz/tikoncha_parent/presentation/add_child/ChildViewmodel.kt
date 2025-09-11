package uz.tikoncha_parent.presentation.add_child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.remote.model.AddChildRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.AddChildUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ChildViewmodel(
    private val addChildUseCase: AddChildUseCase
): ViewModel() {

    private var addChildJob: Job? = null

    private val _state = MutableStateFlow(ChildState())
    val state = _state.asStateFlow()

    fun onEvent(event: ChildEvent){
        when(event){
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
        addChildJob = viewModelScope.launch{


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