package uz.saidburxon.newedu.presentation.feature.create_password

sealed class CreatePasswordEvent {
    data class OnCreatePassword(val password: String): CreatePasswordEvent()
    data class OnCreateConfirmPassword(val confirmPassword: String): CreatePasswordEvent()
    object OnConfirmClicked: CreatePasswordEvent()
    object IsPhoneNumberValid: CreatePasswordEvent()
}