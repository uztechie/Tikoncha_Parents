package uz.tikoncha_parent.presentation.login

import org.jetbrains.compose.resources.StringResource

data class LoginState(
    val number: String = "",
    val showPhone: Boolean = false,
    val isTelegramLoading: Boolean = false,
    val isPhoneLoading: Boolean = false,          // send-otp loading
    val errorMessage: String? = null,             // Telegram inline xato
    val errorRes: org.jetbrains.compose.resources.StringResource? = null,
    val dialogErrorMessage: String? = null,        // phone send-otp dialog xato
    val dialogErrorRes: org.jetbrains.compose.resources.StringResource? = null,
) {
    val isPhoneNumberValid get() = number.length == 9 && number.all { it.isDigit() }
    val fullNumber get() = "+998$number"
}