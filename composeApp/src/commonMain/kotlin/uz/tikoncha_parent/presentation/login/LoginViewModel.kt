package uz.tikoncha_parent.presentation.login

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ScreenModel {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.OnNumberInsert -> {
                val clean = event.number.filter { it.isDigit() }.take(9)
                _state.update {
                    it.copy(
                        number = clean
                    )
                }
            }
        }
    }
}