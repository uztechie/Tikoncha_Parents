package uz.saidburxon.newedu.presentation.feature.login_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginPasswordViewmodel (): ViewModel() {

    private val _state = MutableStateFlow(LoginPasswordState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginPasswordEvent){
        when(event){
            is LoginPasswordEvent.OnLoginPassword -> {
                _state.update {
                    it.copy(
                        password = event.number
                    )
                }
            }

            LoginPasswordEvent.OnConfirmClicked -> {
                val password = _state.value.password
                val cleanedNumber = password.filter { it.isDigit() }
                if (cleanedNumber.length == 6) {
                    _state.update {
                        it.copy(
                            accept = true,
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
        }
    }
}