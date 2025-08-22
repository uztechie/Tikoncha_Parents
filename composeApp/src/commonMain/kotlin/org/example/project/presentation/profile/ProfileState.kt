package org.example.project.presentation.profile

import org.example.project.domain.model.UserInfo
import org.example.project.presentation.domain.model.Child

data class ProfileState(
    val profileImage: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val relativity: String = "",
    val passportNumber: String = "",
    val userInfo: UserInfo? = null,
    val children: List<UserInfo> = emptyList()
)