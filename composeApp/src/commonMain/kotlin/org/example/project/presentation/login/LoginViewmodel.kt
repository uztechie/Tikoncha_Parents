package org.example.project.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.SendOtpUseCase
import uz.saidburxon.newedu.data.model.SendOtpRequest

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
                        loading = false,
                        success = false,
                        errorMessage =  null
                    )
                }
            }
        }
    }


    private fun sendOtp() {

        println("sendOtpp")

        _state.update {
            it.copy(
                loading = true,
                success = false,
                errorMessage =  null
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
                            loading = false,
                            errorMessage = response.message,
                            success = false
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            loading = false,
                            errorMessage = null,
                            success = true
                        )
                    }
                }
            }


        }


    }


}