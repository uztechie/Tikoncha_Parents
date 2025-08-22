package org.example.project.presentation.login

data class LoginState(
    val number: String = "",
    val fullNumber: String = "",
    val accept: Boolean = false,
    var loading: Boolean = false,
    var success: Boolean = false,
    var errorMessage: String? = null,
)
