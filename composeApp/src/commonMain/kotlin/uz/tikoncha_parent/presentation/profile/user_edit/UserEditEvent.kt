package uz.tikoncha_parent.presentation.profile.user_edit

import uz.tikoncha_parent.domain.model.GenderType

sealed interface UserEditEvent {
    data class OnFirstName(val firstName: String) : UserEditEvent
    data class OnLastName(val lastName: String) : UserEditEvent
    data class OnPatronymic(val patronymic: String) : UserEditEvent
    data class OnGender(val gender: GenderType) : UserEditEvent
    data object OnSave : UserEditEvent
    data object ClearError : UserEditEvent
}