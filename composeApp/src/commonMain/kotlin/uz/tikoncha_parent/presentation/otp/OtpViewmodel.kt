package uz.tikoncha_parent.presentation.otp

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.AuthRepository
import uz.tikoncha_parent.domain.repository.SessionRepository
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class OtpViewmodel(
    private val auth: AuthRepository,
    private val session: SessionRepository
) : ScreenModel {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private var verifyOtpJob: Job? = null
    private var sendOtpJob: Job? = null
    private var timerJob: Job? = null

    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.SetPhone -> {
                val change = _state.value.phoneNumber != event.phoneNumber
                _state.update {
                    it.copy(
                        otpCode = "",
                        hasInputError = false,
                        phoneNumber = event.phoneNumber,
                        responseState = ResponseState.Idle
                    )
                }
                if (change || !_state.value.isRunning) {
                    startTimer()
                }
            }

            is OtpEvent.OnOtpUpdate -> {
                val clean = event.otpCode.filter { it.isDigit() }.take(6)
                _state.update {
                    it.copy(
                        otpCode = clean,
                        hasInputError = false
                    )
                }
            }

            OtpEvent.OnConfirmClicked -> {
                verifyOtp()
            }

            OtpEvent.SendOtp -> {
                resendOtp()
            }

            OtpEvent.Reset -> {
                _state.update {
                    it.copy(
                        responseState = ResponseState.Idle
                    )
                }
            }

            OtpEvent.ResetError -> {
                _state.update {
                    it.copy(
                        hasInputError = false,
                        responseState = ResponseState.Idle,
                    )
                }
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

            when (val res = auth.verifyOtp(current.phoneNumber, current.otpCode)) {
                is Outcome.Failure -> {
                    _state.update {
                        it.copy(
                            hasInputError = true,
                            responseState = ResponseState.Error(failure = res),
                        )
                    }
                }

                is Outcome.Success -> {
                    session.save(res.data, phone = current.phoneNumber)
                    _state.update {
                        it.copy(
                            responseState = ResponseState.Success(data = res.data)
                        )
                    }
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

            when (val res = auth.sendOtp(phone)) {
                is Outcome.Success -> {
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

                is Outcome.Failure -> {
                    _state.update {
                        it.copy(
                            isSendingOtp = false,
                            hasInputError = false,
                            responseState = ResponseState.Error(failure = res)
                        )
                    }
                }
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
    }
}