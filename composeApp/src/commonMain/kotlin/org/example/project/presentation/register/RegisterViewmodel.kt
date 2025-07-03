package org.example.project.presentation.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewmodel(): ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent){
        when(event){
            is RegisterEvent.OnNameInsert -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }
            is RegisterEvent.OnFullNameInsert -> {
                _state.update {
                    it.copy(
                        fullName = event.fullName
                    )
                }
            }
            is RegisterEvent.OnMiddleNameInsert -> {
                _state.update {
                    it.copy(
                        middleName = event.middleName
                    )
                }
            }
            is RegisterEvent.OnIdNumberInsert -> {
                _state.update {
                    it.copy(
                        idNumber = event.idNumber
                    )
                }
            }
            is RegisterEvent.OnGenderSelected -> {
                _state.update {
                    it.copy(
                        genderIndex = event.genderIndex,
                    )
                }
            }
            RegisterEvent.OnConfirmClicked -> {}
        }
    }
}