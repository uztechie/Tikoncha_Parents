package uz.tikoncha_parent.presentation.otp

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.VerifyOtpUseCase
import uz.tikoncha_parent.platform.Logger
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.tikoncha_parent.domain.use_case.SendOtpUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class OtpViewmodel(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val sendOtpUseCase: SendOtpUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private var verifyOtpJob: Job? = null

    private var timerJob: Job? = null


    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.OnOtpUpdate -> {
                _state.update {
                    it.copy(
                        otpCode = event.otpCode,
                        hasInputError = false
                    )
                }
            }

            OtpEvent.OnConfirmClicked -> {
                verifyOtp()
            }

            OtpEvent.TimeStart -> {
                startTimer()
            }

            is OtpEvent.SetPhone -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }

            OtpEvent.Reset -> {
                _state.update {
                    it.copy(
                        responseState = ResponseState.Idle
                    )
                }
            }

            OtpEvent.SendOtp -> {
                sentOtp()
            }

            is OtpEvent.SetTelegram -> {
                _state.update {
                    it.copy(
                        isTelegram = event.isTelegram
                    )
                }
            }
        }
    }

    private fun verifyOtp() {
        val phone = _state.value.phoneNumber
        val otp = _state.value.otpCode

        verifyOtpJob?.cancel()
        verifyOtpJob = screenModelScope.launch() {
            _state.update {
                it.copy(
                    responseState = ResponseState.Loading,
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
                            hasInputError = true,
                            responseState = ResponseState.Error(
                                res = response.resId,
                                message = response.message
                            )
                        )
                    }
                }

                is Resource.Success -> {

                    AppSettings.refreshToken = response.data.refresh_token ?: ""
                    AppSettings.accessToken = response.data.access_token ?: ""
                    AppSettings.hasUserLogin = response.data.user_info != null
                    AppSettings.userId = response.data.user_id ?: ""
                    AppSettings.userInfo = response.data.user_info?.toUserInfo()
                    AppSettings.isTestAccount = state.value.phoneNumber.startsWith("+99811")

                    Logger.d(
                        "OtpViewModel",
                        "verifyOtp: userLogin = ${response.data.user_info != null} phone=${state.value.phoneNumber}  hasUserLogin=${AppSettings.hasUserLogin}  refreshToken=${AppSettings.refreshToken}"
                    )

                    Logger.d(
                        "OtpViewModel",
                        "userInfo = ${AppSettings.userInfo}"
                    )

                    _state.update {
                        it.copy(
                            responseState = ResponseState.Success(
                                data = response.data
                            )
                        )
                    }
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = screenModelScope.launch {
            _state.update { it.copy(timeLife = 60, isRunning = true) }
            while (_state.value.timeLife > 0) {
                delay(1000)
                _state.update { it.copy(timeLife = it.timeLife - 1) }
            }
            _state.update { it.copy(isRunning = false) }
        }
    }

    private fun sentOtp() {
        val phone = state.value.phoneNumber
        if (phone.isBlank()) return

        screenModelScope.launch {
            val request = SendOtpRequest(phone = phone)
            when (val res = sendOtpUseCase(request)) {
                is Resource.Success -> {
                    startTimer()
                    _state.update {
                        it.copy(
                            otpCode = "",
                            responseState = ResponseState.Idle
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Error(
                                res = res.resId,
                                message = res.message,
                            ),
                            deleteAccountUrl = res.data
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }
}