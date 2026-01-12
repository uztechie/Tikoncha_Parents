package uz.tikoncha_parent.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.mapper.toUserInfo
import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.use_case.RegisterUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class RegisterViewmodel(
    private val registerUseCase: RegisterUseCase
): ScreenModel {

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
            is RegisterEvent.OnLastNameInsert -> {
                _state.update {
                    it.copy(
                        lastName = event.fullName
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
                        registerResponseState = ResponseState.Idle

                    )
                }
            }
        }
    }


    private fun requestRegistration(){
        registerJob?.cancel()
        registerJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    registerResponseState = ResponseState.Loading

                )
            }
            val request = RegisterUserRequest(
                user_id = AppSettings.userId,
                first_name = _state.value.name?:"",
                last_name = _state.value.lastName?:"",
                patronymic = _state.value.middleName?:"",
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
                            registerResponseState = ResponseState.Error(
                                res = result.resId,
                                message = result.message
                            )
                        )
                    }
                }
                is Resource.Success -> {
                    AppSettings.hasUserLogin = true
                    AppSettings.userInfo = result.data.toUserInfo()
                    _state.update {
                        it.copy(
                            registerResponseState = ResponseState.Success()
                        )
                    }
                }
            }
        }
    }
}