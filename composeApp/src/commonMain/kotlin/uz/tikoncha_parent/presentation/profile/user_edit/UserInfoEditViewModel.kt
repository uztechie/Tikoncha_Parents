package uz.tikoncha_parent.presentation.profile.user_edit

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.maydonlar_toliq_toldirilmagan
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.use_case.UserInfoEditUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class UserInfoEditViewModel(
    private val userInfoEditUseCase: UserInfoEditUseCase,
    private val userInfo: UserInfo
): ScreenModel {

    private val _state = MutableStateFlow(
        UserEditState(
            firstName = userInfo.name,
            lastName = userInfo.lastName,
            patronymic = userInfo.patronymic,
            genderType = userInfo.genderType
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: UserEditEvent){
        when(event){
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
                save()
            }
        }
    }

    private fun save(){

        val current = state.value

        if (current.firstName.isBlank() || current.lastName.isBlank() || current.patronymic.isBlank()){
            _state.update {
                it.copy(
                    saveState = ResponseState.Error(res = Res.string.maydonlar_toliq_toldirilmagan),
                )
            }
            return
        }

        val age = current.age.toIntOrNull()

        val request = RegisterUserRequest(
            user_id = userInfo.userId,
            first_name = current.firstName,
            last_name = current.lastName,
            patronymic = current.patronymic,
            age = age ?: userInfo.age,
            gender = when (current.genderType) {
                GenderType.MALE -> "male"
                GenderType.FEMALE -> "female"
            },
            passport_id = userInfo.passportId ?: ""
        )

        screenModelScope.launch {
            _state.update {
                it.copy(
                    saveState = ResponseState.Loading
                )
            }

            val req = userInfoEditUseCase.invoke(request)
            when (req) {
                is Resource.Success -> {
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

                is Resource.Error -> {
                    val message = req.message ?: "Xatolik"
                    _state.update { it.copy(saveState = ResponseState.Error(message = message)) }
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            saveState = ResponseState.Loading
                        )
                    }
                }
            }
        }
    }
}