package uz.tikoncha_parent.presentation.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpResponseData
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.auth.TelegramAuthResult
import uz.tikoncha_parent.domain.repository.TelegramAuthRepository
import uz.tikoncha_parent.domain.use_case.SendOtpUseCase
import uz.tikoncha_parent.domain.use_case.auth.TelegramLoginUseCase
import uz.tikoncha_parent.platform.BuildConfig

class LoginViewModel(
    private val telegramAuth: TelegramAuthRepository,
    private val telegramLoginUseCase: TelegramLoginUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
) : ScreenModel {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<LoginSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var exchangeJob: Job? = null
    private var sendOtpJob: Job? = null

    init {
        val telegramInstalled = telegramAuth.isTelegramInstalled()
        _state.update { it.copy(showPhone = !telegramInstalled || BuildConfig.isDebug) }
        observeTelegramResult()
    }

    private fun observeTelegramResult() {
        screenModelScope.launch {
            telegramAuth.pendingResult.collect { result ->
                if (result == null) return@collect
                handleTelegramResult(result)
                telegramAuth.consumePending()
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnNumberInsert -> {
                val clean = event.number.filter { it.isDigit() }.take(9)
                _state.update { it.copy(number = clean) }
            }
            LoginEvent.OnTelegramClicked -> handleTelegramClick()
            LoginEvent.OnPhoneContinue -> handlePhoneContinue()
            LoginEvent.OnErrorDismissed ->
                _state.update { it.copy(errorMessage = null, errorRes = null) }
            LoginEvent.OnDialogErrorDismissed ->
                _state.update { it.copy(dialogErrorMessage = null, dialogErrorRes = null) }
        }
    }

    // ---------- Telegram ----------
    private fun handleTelegramClick() {
        if (_state.value.isTelegramLoading) return
        val started = telegramAuth.startLogin()
        if (started) {
            _state.update { it.copy(isTelegramLoading = true, errorMessage = null, errorRes = null) }
        } else {
            _state.update {
                it.copy(
                    isTelegramLoading = false,
                    showPhone = true,
                    errorRes = Res.string.kutilmagan_xatolik_qayta_urining,
                )
            }
        }
    }

    private fun handleTelegramResult(result: TelegramAuthResult) {
        when (result) {
            TelegramAuthResult.Cancelled ->
                _state.update { it.copy(isTelegramLoading = false, showPhone = true) }
            is TelegramAuthResult.Error ->
                _state.update {
                    it.copy(isTelegramLoading = false, showPhone = true, errorMessage = result.message)
                }
            is TelegramAuthResult.Success -> exchange(result.idToken)
        }
    }

    private fun exchange(idToken: String) {
        exchangeJob?.cancel()
        exchangeJob = screenModelScope.launch {
            _state.update { it.copy(isTelegramLoading = true, errorMessage = null, errorRes = null) }
            when (val res = telegramLoginUseCase(idToken)) {
                is Resource.Loading -> {}
                is Resource.Error -> _state.update {
                    it.copy(
                        isTelegramLoading = false,
                        showPhone = true,
                        errorRes = res.resId,
                        errorMessage = res.message,
                    )
                }
                is Resource.Success -> {
                    saveSession(res.data)
                    _state.update { it.copy(isTelegramLoading = false) }
                    if (res.data.user_info == null) _sideEffects.send(LoginSideEffect.NavigateToRegister)
                    else _sideEffects.send(LoginSideEffect.NavigateToHome)
                }
            }
        }
    }

    // ---------- Phone OTP ----------
    private fun handlePhoneContinue() {
        val current = _state.value
        if (!current.isPhoneNumberValid) return
        if (current.isPhoneLoading) return

        sendOtpJob?.cancel()
        sendOtpJob = screenModelScope.launch {
            _state.update { it.copy(isPhoneLoading = true, dialogErrorMessage = null, dialogErrorRes = null) }

            when (val res = sendOtpUseCase(SendOtpRequest(phone = current.fullNumber))) {
                is Resource.Loading -> {}
                is Resource.Error -> _state.update {
                    it.copy(
                        isPhoneLoading = false,
                        dialogErrorMessage = res.message,
                        dialogErrorRes = res.resId,
                    )
                }
                is Resource.Success -> {
                    _state.update { it.copy(isPhoneLoading = false) }
                    _sideEffects.send(LoginSideEffect.NavigateToOtp(current.fullNumber))
                }
            }
        }
    }

    private fun saveSession(data: VerifyOtpResponseData) {
        AppSettings.refreshToken = data.refresh_token ?: ""
        AppSettings.accessToken = data.access_token ?: ""
        AppSettings.hasUserLogin = data.user_info != null
        AppSettings.userId = data.user_id ?: ""
        AppSettings.userInfo = data.user_info?.toUserInfo()
    }
}