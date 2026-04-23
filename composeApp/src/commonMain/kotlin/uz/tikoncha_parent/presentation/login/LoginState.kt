package uz.tikoncha_parent.presentation.login

data class LoginState(
    val number: String = "",
) {
    val isPhoneNumberValid: Boolean
        get() = number.length == 9 &&
                number.all { it.isDigit() } &&
                (number.take(2).toIntOrNull()?.let { it in 10..99 } == true)

    val fullNumber: String
        get() = if (isPhoneNumberValid) "+998$number" else ""
}
