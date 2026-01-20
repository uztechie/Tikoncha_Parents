package uz.tikoncha_parent.presentation.profile.child_user_edit

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.maydonlar_toliq_toldirilmagan
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.use_case.ChildInfoEditUseCase
import uz.tikoncha_parent.presentation.ui_state.ResponseState

class ChildInfoEditViewModel(
    private val childInfoEditUseCase: ChildInfoEditUseCase,
    private val child: UserInfo
): ScreenModel {

    private val _state = MutableStateFlow(
        ChildEditState(
            firstName = child.name,
            lastName = child.lastName,
            patronymic = child.patronymic,
            age = child.age?.toString().orEmpty(),
            genderType = child.genderType
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: ChildEditEvent){
        when(event){
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

    private fun save(){

        val state = state.value

        if (state.firstName.isBlank() || state.lastName.isBlank() || state.patronymic.isBlank()){
            _state.update {
                it.copy(
                    saveState = ResponseState.Error(res = Res.string.maydonlar_toliq_toldirilmagan),
                )
            }
            return
        }

        val age = state.age.toIntOrNull()

        val request = UserInfoDto(
            user_id = child.userId,
            first_name = state.firstName,
            last_name = state.lastName,
            patronymic = state.patronymic,
            age = age,
            gender = when (state.genderType){
                GenderType.MALE -> "male"
                GenderType.FEMALE -> "female"
            }
        )

        screenModelScope.launch {
            _state.update {
                it.copy(
                    saveState = ResponseState.Loading
                )
            }

            val req = childInfoEditUseCase.invoke(request)
            when(req){
                is Resource.Success -> {
                    AppSettings.selectedChild
                    _state.update {
                        it.copy(
                            saveState = ResponseState.Success()
                        )
                    }
                }
                is Resource.Error -> {
                    val error = childInfoEditUseCase.invoke(request)
                    val message = (error as Resource.Error).message ?: "Xatolik"

                    _state.update {
                        it.copy(
                            saveState = ResponseState.Error(message = message)
                        )
                    }
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