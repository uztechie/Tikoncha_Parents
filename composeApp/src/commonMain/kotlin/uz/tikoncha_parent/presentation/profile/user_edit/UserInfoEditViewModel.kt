package uz.tikoncha_parent.presentation.profile.user_edit

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.LoginRepository
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class UserInfoEditViewModel(
    private val loginRepository: LoginRepository,
    private val userInfo: UserInfo
) : ScreenModel {

    private var saveJob: Job? = null
    private val _state = MutableStateFlow(
        UserEditState(
            firstName = userInfo.name,
            lastName = userInfo.lastName,
            patronymic = userInfo.patronymic,
            genderType = userInfo.genderType,

            // Original qiymatlar — dirty check uchun
            originalFirstName = userInfo.name,
            originalLastName = userInfo.lastName,
            originalPatronymic = userInfo.patronymic,
            originalGenderType = userInfo.genderType
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: UserEditEvent) {
        when (event) {
            is UserEditEvent.OnFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }

            is UserEditEvent.OnLastName -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }

            is UserEditEvent.OnPatronymic -> {
                _state.update {
                    it.copy(
                        patronymic = event.patronymic
                    )
                }
            }

            is UserEditEvent.OnGender -> {
                _state.update {
                    it.copy(
                        genderType = event.gender
                    )
                }
            }

            UserEditEvent.ClearError -> {
                _state.update {
                    it.copy(
                        saveState = ResponseState.Idle
                    )
                }
            }

            UserEditEvent.OnSave -> {
                if (state.value.isFormValid) save()
            }
        }
    }

    private fun save() {
        saveJob?.cancel()
        saveJob = screenModelScope.launch {
            _state.update {
                it.copy(
                    saveState = ResponseState.Loading
                )
            }

            val current = state.value
            val request = RegisterUserRequest(
                user_id = userInfo.userId,
                last_name = current.lastName.trim(),
                first_name = current.firstName.trim(),
                patronymic = current.patronymic.trim(),
                passport_id = userInfo.passportId ?: "",
                age = current.age.toIntOrNull() ?: userInfo.age,
                gender = when (current.genderType) {
                    GenderType.MALE -> "male"
                    GenderType.FEMALE -> "female"
                }
            )

            when (val res = loginRepository.userInfoEdit(request)) {
                is Outcome.Success -> {
                    AppSettings.userInfo = userInfo.copy(
                        name = current.firstName,
                        lastName = current.lastName,
                        patronymic = current.patronymic,
                        genderType = current.genderType
                    )

                    _state.update {
                        it.copy(
                            saveState = ResponseState.Success()
                        )
                    }
                }

                is Outcome.Failure -> {
                    _state.update {
                        it.copy(
                            saveState = ResponseState.Error(failure = res)
                        )
                    }
                }
            }
        }
    }
}