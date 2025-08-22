package org.example.project.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.local.AppSettings
import org.example.project.data.mapper.toUserInfo
import org.example.project.data.remote.model.RegisterUserRequest
import org.example.project.domain.model.GenderType
import org.example.project.domain.model.Resource
import org.example.project.domain.use_case.RegisterUseCase

class RegisterViewmodel(
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private var registerJob: Job? = null

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
            RegisterEvent.OnConfirmClicked -> {
                requestRegistration()
            }

            RegisterEvent.Reset -> {
                _state.update {
                    it.copy(
                        registerLoading = false,
                        registerError = "",
                        registerSuccess = false

                    )
                }
            }
        }
    }


    private fun requestRegistration(){
        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    registerLoading = true,
                    registerError = "",
                    registerSuccess = false

                )
            }
            val request = RegisterUserRequest(
                user_id = AppSettings.userId,
                first_name = _state.value.name?:"",
                last_name = _state.value.name?:"",
                patronymic = _state.value.name?:"",
                gender = GenderType.getGenderByIndex(_state.value.genderIndex).key,
                age = 0,
                passport_id = _state.value.idNumber
            )

            val result = registerUseCase(request)

            when(result){
                is Resource.Loading -> {}
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            registerLoading = false,
                            registerError = result.message,
                            registerSuccess = false
                        )
                    }
                }
                is Resource.Success -> {
                    AppSettings.hasUserLogin = true
                    AppSettings.userInfo = result.data.toUserInfo()
                    _state.update {
                        it.copy(
                            registerLoading = false,
                            registerError = "",
                            registerSuccess = true
                        )
                    }
                }
            }
        }
    }
}