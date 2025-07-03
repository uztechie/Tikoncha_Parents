package org.example.project.presentation.register

data class RegisterState(
    val name: String = "",
    val fullName: String = "",
    val middleName: String = "",
    val idNumber: String = "",
    val genderIndex: Int = 0,
    val accept: Boolean = false
)
