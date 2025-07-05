package org.example.project.presentation.register

sealed class RegisterEvent {
    data class OnNameInsert(val name: String): RegisterEvent()
    data class OnFullNameInsert(val fullName: String): RegisterEvent()
    data class OnMiddleNameInsert(val middleName: String): RegisterEvent()
    data class OnIdNumberInsert(val idNumber: String): RegisterEvent()
    data class OnGenderSelected(val genderIndex: Int): RegisterEvent()
    object OnConfirmClicked: RegisterEvent()
}