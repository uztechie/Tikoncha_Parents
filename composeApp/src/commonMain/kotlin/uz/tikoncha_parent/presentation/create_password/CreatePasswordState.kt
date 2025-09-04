package uz.saidburxon.newedu.presentation.feature.create_password

data class CreatePasswordState(
    val password: String = "",
    val confirmPassword: String = "",
    val accept: Boolean = false
)
