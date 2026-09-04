package uz.tikoncha_parent.presentation.profile.child_user_edit

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.LoginRepository
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ChildInfoEditViewModel(
    private val loginRepository: LoginRepository,
    private val child: UserInfo
) : ScreenModel {

    private var saveJob: Job? = null
    private val _state = MutableStateFlow(
        ChildEditState(
            firstName = child.name,
            lastName = child.lastName,
            patronymic = child.patronymic,
            age = child.age?.toString().orEmpty(),
            genderType = child.genderType,

            // Original qiymatlar — dirty check uchun
            originalFirstName = child.name,
            originalLastName = child.lastName,
            originalPatronymic = child.patronymic,
            originalGenderType = child.genderType
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: ChildEditEvent) {
        when (event) {
            is ChildEditEvent.OnFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName
                    )
                }
            }

            is ChildEditEvent.OnLastName -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName
                    )
                }
            }

            is ChildEditEvent.OnPatronymic -> {
                _state.update {
                    it.copy(
                        patronymic = event.patronymic
                    )
                }
            }

            is ChildEditEvent.OnAge -> {
                _state.update {
                    it.copy(
                        age = event.age
                    )
                }
            }

            is ChildEditEvent.OnGender -> {
                _state.update {
                    it.copy(
                        genderType = event.gender
                    )
                }
            }

            ChildEditEvent.ClearError -> {
                _state.update {
                    it.copy(
                        saveState = ResponseState.Idle
                    )
                }
            }

            ChildEditEvent.OnSave -> {
                save()
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
            val state = state.value

            val request = UserInfoDto(
                user_id = child.userId,
                first_name = state.firstName,
                last_name = state.lastName,
                patronymic = state.patronymic,
                age = state.age.toIntOrNull(),
                gender = when (state.genderType) {
                    GenderType.MALE -> "male"
                    GenderType.FEMALE -> "female"
                }
            )

            when (val res = loginRepository.childInfoEdit(request)) {
                is Outcome.Success -> {
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