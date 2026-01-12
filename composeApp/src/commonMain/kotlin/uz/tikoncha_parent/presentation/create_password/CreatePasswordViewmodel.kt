package uz.tikoncha_parent.presentation.create_password

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.saidburxon.newedu.presentation.feature.create_password.CreatePasswordEvent
import uz.saidburxon.newedu.presentation.feature.create_password.CreatePasswordState

class CreatePasswordViewmodel (): ScreenModel {

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