package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.RefreshTokenResponse
import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.data.remote.model.RegisterUserResponse
import uz.tikoncha_parent.data.remote.model.SendOtpRequest
import uz.tikoncha_parent.data.remote.model.SendOtpResponse
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.data.remote.model.UserInfoResponse
import uz.tikoncha_parent.data.remote.model.VerifyOtpRequest
import uz.tikoncha_parent.data.remote.model.VerifyOtpResponse
import uz.tikoncha_parent.data.remote.model.auth.TelegramLoginRequest
import uz.tikoncha_parent.data.remote.model.auth.TelegramLoginResponse


interface LoginRepository {

    suspend fun telegramLogin(request: TelegramLoginRequest): TelegramLoginResponse
    suspend fun sendOtp(request: SendOtpRequest): SendOtpResponse
    suspend fun verifyOtp(request: VerifyOtpRequest): VerifyOtpResponse
    suspend fun refreshToken(): RefreshTokenResponse
    suspend fun userInfo(): UserInfoResponse
    suspend fun userInfoEdit(request: RegisterUserRequest): RegisterUserResponse
    suspend fun childInfoEdit(body: UserInfoDto): UserInfoResponse
}