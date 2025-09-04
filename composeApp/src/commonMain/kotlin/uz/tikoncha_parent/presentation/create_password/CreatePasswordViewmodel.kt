package uz.saidburxon.newedu.presentation.feature.create_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreatePasswordViewmodel (): ViewModel() {

    private val _state = MutableStateFlow(CreatePasswordState())
    val state = _state.asStateFlow()

    fun onEvent(event: CreatePasswordEvent){
        when(event){
            is CreatePasswordEvent.OnCreatePassword -> {
                _state.update {
                    it.copy(
                        password = event.password
                    )
                }
            }
            is CreatePasswordEvent.OnCreateConfirmPassword -> {
                _state.update {
                    it.copy(
                        confirmPassword = event.confirmPassword
                    )
                }
            }

            CreatePasswordEvent.OnConfirmClicked -> {
                val password = _state.value.password
                val confirmPassword = _state.value.confirmPassword
                if (password == confirmPassword) {
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

            CreatePasswordEvent.IsPhoneNumberValid -> {
                if (state.value.password.isNotEmpty() == state.value.confirmPassword.isNotEmpty() || state.value.password == state.value.confirmPassword) {
                    _state.update {
                        it.copy(
                            accept = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            accept = false
                        )
                    }
                }
            }
        }
    }
}