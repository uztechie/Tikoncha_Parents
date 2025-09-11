package uz.tikoncha_parent.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.SendOtpUseCase
import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class LoginViewmodel(
    private val sendOtpUseCase: SendOtpUseCase
): ViewModel() {

    private var sendOptJob: Job? = null

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.OnNumberInsert -> {
                _state.update {
                    it.copy(
                        number = event.number,
                        fullNumber = "+998${event.number}"
                    )
                }
            }

            LoginEvent.OnConfirmClicked -> {
                sendOtp()
            }

            LoginEvent.Reset -> {
                _state.update {
                    it.copy(
                        responseState = ResponseState.Idle,
                    )
                }
            }
        }
    }


    private fun sendOtp() {

        println("sendOtpp")

        _state.update {
            it.copy(
                responseState = ResponseState.Loading,
            )
        }

        val phone = _state.value.fullNumber
        sendOptJob?.cancel()
        sendOptJob = viewModelScope.launch{


            val request = SendOtpRequest(phone = phone)
            val response = sendOtpUseCase(request)
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Error(
                                res = response.resId,
                                message = response.message
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Success()
                        )
                    }
                }
            }


        }


    }


}