package org.example.project.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewmodel(): ViewModel() {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private var timerJob: Job? = null


    fun onEvent(event: OtpEvent){
        when(event){
            is OtpEvent.OnOtpUpdate -> {
                _state.update {
                    it.copy(
                        otpCode = event.otpCode
                    )
                }
            }

            OtpEvent.OnConfirmClicked -> {
                val otp = _state.value.otpCode
                val cleanedOtp = otp.filter { it.isDigit() }
                if (cleanedOtp.length == 6) {
                    _state.update {
                        it.copy(
                            accept = true,
//                            isUserExists = Utils.userPhones.contains(state.value.phoneNumber)
                        )
                    }

                }else{
                    _state.update {
                        it.copy(
                            accept = false,
                        )
                    }
                }
            }

            OtpEvent.TimeStart -> {
                startTimer()
            }

            OtpEvent.Tick -> {
                val current = _state.value.timeLife
                if (current > 0) {
                    _state.value = _state.value.copy(timeLife = current - 1)
                } else {
                    timerJob?.cancel()
                    _state.value = _state.value.copy(isRunning = false)
                }
            }

            is OtpEvent.SetPhone -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }
            OtpEvent.ClearNavigation -> {
                _state.update {
                    it.copy(
                        isUserExists = null
                    )
                }
            }
        }
    }
    private fun startTimer() {
        _state.value = _state.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_state.value.timeLife > 0) {
                delay(1000L)
                onEvent(OtpEvent.Tick)
            }
        }
    }
}