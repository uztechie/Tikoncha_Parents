package uz.tikoncha_parent.presentation.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.RegistrationData
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.AuthRepository
import uz.tikoncha_parent.domain.repository.SessionRepository
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class RegisterViewmodel(
    private val auth: AuthRepository,
    private val session: SessionRepository
) : ScreenModel {

    private var registerJob: Job? = null

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
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
                        lastName = event.lastName
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


    private fun requestRegistration() {
        registerJob?.cancel()
        registerJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    registerResponseState = ResponseState.Loading
                )
            }
            val data = RegistrationData(
                userId = session.currentUserId,
                firstName = _state.value.name,
                lastName = _state.value.lastName,
                patronymic = _state.value.middleName,
                passportId = _state.value.idNumber,
                gender = GenderType.getGenderByIndex(_state.value.genderIndex)
            )

            when (val res = auth.register(data)) {
                is Outcome.Failure -> {
                    _state.update {
                        it.copy(
                            registerResponseState = ResponseState.Error(failure = res)
                        )
                    }
                }

                is Outcome.Success -> {
                    session.saveUserInfo(res.data)
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