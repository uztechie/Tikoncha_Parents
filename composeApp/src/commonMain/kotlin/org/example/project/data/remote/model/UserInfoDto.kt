package org.example.project.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class UserInfoDto(
    val user_id: String? = null,
    val phone_number: String? = null,
    val first_name: String?,
    val last_name: String?,
    val patronymic: String?,
    val age: Int?,
    val gender: String?,
    val school_id: String? = null,
    val school_name: String? = null,
    val school_class_name: String? = null,
    val school_class_id: String? = null,
    val shift: String? = null,
    val passport_id: String? = null
)
