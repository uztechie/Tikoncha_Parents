package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.domain.model.RegistrationData

fun RegistrationData.toRequest(): RegisterUserRequest = RegisterUserRequest(
    user_id = userId,
    first_name = firstName,
    last_name = lastName,
    patronymic = patronymic,
    age = 0,
    gender = gender.key,
    passport_id = passportId
)