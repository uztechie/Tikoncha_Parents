package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val user_id: String,
    val first_name: String,
    val last_name: String,
    val patronymic: String,
    val age: Int,
    val gender: String,
    val passport_id: String
)
