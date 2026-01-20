package uz.tikoncha_parent.presentation.profile.child_user_edit

import uz.tikoncha_parent.domain.model.GenderType

sealed interface ChildEditEvent {
    data class OnFirstName(val firstName: String) : ChildEditEvent
    data class OnLastName(val lastName: String) : ChildEditEvent
    data class OnPatronymic(val patronymic: String) : ChildEditEvent
    data class OnAge(val age: String) : ChildEditEvent
    data class OnGender(val gender: GenderType) : ChildEditEvent
    data object OnSave : ChildEditEvent
    data object ClearError : ChildEditEvent
}