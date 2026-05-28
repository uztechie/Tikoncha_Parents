package uz.tikoncha_parent.presentation.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.use_case.auth.TelegramLoginUseCase

class LoginViewModel(
//    private val telegramLoginUseCase: TelegramLoginUseCase
): ScreenModel {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()


    private val _sideEffects = Channel<LoginSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnNumberInsert -> {
                val clean = event.number.filter { it.isDigit() }.take(9)
                _state.update { it.copy(number = clean) }
            }

            LoginEvent.OnTelegramClicked -> {}

            LoginEvent.OnErrorDismissed -> {
                _state.update { it.copy(errorMessage = null) }
            }
        }
    }

//    private fun handleTelegramLogin() {
//        if (_state.value.isTelegramLoading) return
//
//        screenModelScope.launch {
//            _state.update { it.copy(isTelegramLoading = true, errorMessage = null) }
//
//            when (val result = telegramLoginUseCase()) {
//                LoginResult.Cancelled -> {
//                    _state.update { it.copy(isTelegramLoading = false) }
//                }
//
//                is LoginResult.Failed -> {
//                    _state.update {
//                        it.copy(isTelegramLoading = false, errorMessage = result.message)
//                    }
//                    _sideEffects.send(LoginSideEffect.ShowError(result.message))
//                }
//
//                is LoginResult.Success -> {
//                    _state.update { it.copy(isTelegramLoading = false) }
//                    _sideEffects.send(LoginSideEffect.NavigateToHome)
//                }
//            }
//        }
//    }
}