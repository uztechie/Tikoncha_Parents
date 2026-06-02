package uz.tikoncha_parent.presentation.login

sealed interface LoginSideEffect {
    data object NavigateToHome : LoginSideEffect
    data object NavigateToRegister : LoginSideEffect
    data class NavigateToOtp(val phoneNumber: String) : LoginSideEffect
}