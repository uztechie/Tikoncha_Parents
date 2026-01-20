package uz.tikoncha_parent.presentation.profile.child_user_edit

import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.presentation.ui_state.ResponseState

data class ChildEditState(
    val firstName: String = "",
    val lastName: String = "",
    val patronymic: String = "",
    val age: String = "",
    val genderType: GenderType = GenderType.MALE,

    val saveState: ResponseState<Unit> = ResponseState.Idle
)