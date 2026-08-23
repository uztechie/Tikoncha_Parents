package uz.tikoncha_parent.domain.model

data class RegistrationData(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val passportId: String,
    val gender: GenderType,
)