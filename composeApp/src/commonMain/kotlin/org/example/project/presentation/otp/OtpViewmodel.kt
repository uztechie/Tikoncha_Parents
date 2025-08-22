package org.example.project.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.local.AppSettings
import org.example.project.data.mapper.toUserInfo
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.VerifyOtpUseCase
import org.example.project.platform.Logger
import uz.saidburxon.newedu.data.model.VerifyOtpRequest

class OtpViewmodel(
    private val verifyOtpUseCase: VerifyOtpUseCase
): ViewModel() {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private var verifyOtpJob: Job? = null

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
                verifyOtp()
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

            OtpEvent.Reset -> {
                _state.update {
                    it.copy(
                        loading = false,
                        success = false,
                        errorMessage = null,
                        data = null
                    )
                }
            }
        }
    }

    private fun verifyOtp() {
        val phone = _state.value.phoneNumber
        val otp = _state.value.otpCode

        verifyOtpJob?.cancel()
        verifyOtpJob = viewModelScope.launch() {
            _state.update {
                it.copy(
                    loading = true,
                    success = false,
                    errorMessage = null,
                    data = null
                )
            }
            val request = VerifyOtpRequest(
                phone = phone,
                otp_code = otp
            )
            val response = verifyOtpUseCase(request)
            when (response) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(

                            loading = false,
                            errorMessage = response.message,
                            success = false,
                            data = null

                        )
                    }
                }

                is Resource.Success -> {

                    AppSettings.refreshToken = response.data.refresh_token?:""
                    AppSettings.accessToken = response.data.access_token?:""
                    AppSettings.hasUserLogin = response.data.user_info != null
                    AppSettings.userId = response.data.user_id?:""
                    AppSettings.userInfo = response.data.user_info?.toUserInfo()

                    Logger.d("TAG", "verifyOtp: userLogin = ${response.data.user_info != null}   hasUserLogin=${AppSettings.hasUserLogin}  refreshToken=${AppSettings.refreshToken}")

                    _state.update {
                        it.copy(
                            loading = false,
                            errorMessage = null,
                            data = response.data,
                            success = true
                        )
                    }
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