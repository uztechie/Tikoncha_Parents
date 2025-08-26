package org.example.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val userId: String,
    val phoneNumber: String,
    val fullName: String,
    val name: String,
    val lastName: String,
    val patronymic: String,
    val genderType: GenderType,
    val passportId: String? = null,
    val age:Int? = null,
    val schoolId: String? = null,
    val schoolName: String? = null,
    val schoolClassName: String? = null,
    val schoolClassId: String? = null,
    val shift: String? = null,
){
    override fun toString(): String {
        return fullName
    }
}
