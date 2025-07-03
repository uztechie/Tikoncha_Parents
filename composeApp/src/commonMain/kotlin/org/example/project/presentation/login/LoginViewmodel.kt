package org.example.project.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewmodel(): ViewModel() {

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

            }
        }
    }
}