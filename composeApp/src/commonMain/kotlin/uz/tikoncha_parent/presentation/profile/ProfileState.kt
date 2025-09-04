package uz.tikoncha_parent.presentation.profile

import uz.tikoncha_parent.domain.model.UserInfo

data class ProfileState(
    val profileImageUrl: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val relativity: String = "",
    val passportNumber: String = "",
    val userInfo: UserInfo? = null,
    val children: List<UserInfo> = emptyList()
)