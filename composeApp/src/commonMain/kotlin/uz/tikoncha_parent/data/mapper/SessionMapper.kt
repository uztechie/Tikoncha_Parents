package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.data.remote.model.VerifyOtpResponseData
import uz.tikoncha_parent.domain.model.Session

fun VerifyOtpResponseData.toSession(): Session = Session(
    accessToken = access_token.orEmpty(),
    refreshToken = refresh_token.orEmpty(),
    userId = user_id.orEmpty(),
    userInfo = user_info?.toUserInfo()
)