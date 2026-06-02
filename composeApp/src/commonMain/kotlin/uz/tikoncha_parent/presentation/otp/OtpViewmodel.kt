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
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.SendOtpUseCase
import uz.tikoncha_parent.domain.use_case.VerifyOtpUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class OtpViewmodel(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private var verifyOtpJob: Job? = null
    private var sendOtpJob: Job? = null
    private var timerJob: Job? = null

    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.SetPhone -> {
                if (_state.value.phoneNumber.isEmpty()) {
                    _state.update { it.copy(phoneNumber = event.phoneNumber) }
                    startTimer() // kod login ekranida yuborilgan — timer darrov ketadi
                }
            }

            is OtpEvent.OnOtpUpdate -> {
                val clean = event.otpCode.filter { it.isDigit() }.take(6)
                _state.update { it.copy(otpCode = clean, hasInputError = false) }
            }

            OtpEvent.OnConfirmClicked -> verifyOtp()
            OtpEvent.SendOtp -> resendOtp()

            OtpEvent.Reset -> _state.update { it.copy(responseState = ResponseState.Idle) }

            OtpEvent.ResetError -> _state.update {
                it.copy(
                    responseState = ResponseState.Idle,
                    deleteAccountUrl = null,
                    hasInputError = false,
                )
            }
        }
    }

    private fun verifyOtp() {
        val current = _state.value
        if (current.otpCode.length != 6) return
        if (current.responseState is ResponseState.Loading) return

        verifyOtpJob?.cancel()
        verifyOtpJob = screenModelScope.launch {
            _state.update { it.copy(responseState = ResponseState.Loading) }

            val request = VerifyOtpRequest(phone = current.phoneNumber, otp_code = current.otpCode)
            when (val response = verifyOtpUseCase(request)) {
                is Resource.Loading -> {}
                is Resource.Error -> _state.update {
                    it.copy(
                        hasInputError = true,
                        responseState = ResponseState.Error(res = response.resId, message = response.message),
                    )
                }
                is Resource.Success -> {
                    val data = response.data
                    AppSettings.refreshToken = data.refresh_token ?: ""
                    AppSettings.accessToken = data.access_token ?: ""
                    AppSettings.hasUserLogin = data.user_info != null
                    AppSettings.userId = data.user_id ?: ""
                    AppSettings.userInfo = data.user_info?.toUserInfo()
                    AppSettings.isTestAccount = current.phoneNumber.startsWith(TEST_ACCOUNT_PREFIX)

                    _state.update { it.copy(responseState = ResponseState.Success(data = data)) }
                }
            }
        }
    }

    private fun resendOtp() {
        val phone = _state.value.phoneNumber
        if (phone.isBlank()) return
        if (_state.value.isSendingOtp) return
        if (_state.value.isRunning) return // timer ketayotganda qayta yuborilmaydi

        sendOtpJob?.cancel()
        sendOtpJob = screenModelScope.launch {
            _state.update { it.copy(isSendingOtp = true) }

            when (val res = sendOtpUseCase(SendOtpRequest(phone = phone))) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            otpCode = "",
                            isSendingOtp = false,
                            hasInputError = false,
                            responseState = ResponseState.Idle,
                        )
                    }
                    startTimer()
                }
                is Resource.Error -> _state.update {
                    it.copy(
                        isSendingOtp = false,
                        responseState = ResponseState.Error(res = res.resId, message = res.message),
                        deleteAccountUrl = res.data,
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = screenModelScope.launch {
            _state.update { it.copy(timeLife = TIMER_SECONDS, isRunning = true) }
            while (_state.value.timeLife > 0) {
                delay(1000)
                _state.update { it.copy(timeLife = it.timeLife - 1) }
            }
            _state.update { it.copy(isRunning = false) }
        }
    }

    companion object {
        private const val TIMER_SECONDS = 60
        private const val TEST_ACCOUNT_PREFIX = "+99811"
    }
}