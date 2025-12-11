package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.logout.LogoutResponseDto
import uz.tikoncha_parent.presentation.new_home.logout.LogoutType
import uz.tikoncha_parent.presentation.new_home.logout.LogoutUi

fun LogoutResponseDto.toLogoutUi(): LogoutUi {
    return LogoutUi(
        requestId = id,
        type = LogoutType.fromString(action)
    )
}