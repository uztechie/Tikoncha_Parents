package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.LoginApiService
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
import uz.tikoncha_parent.domain.repository.LoginRepository

class LoginRepositoryImpl(private val api: LoginApiService): LoginRepository {
    override suspend fun telegramLogin(request: TelegramLoginRequest): TelegramLoginResponse {
        return api.telegramLogin(request)
    }

    override suspend fun sendOtp(request: SendOtpRequest): SendOtpResponse {
        return api.sendOtp(request)
    }

    override suspend fun verifyOtp(request: VerifyOtpRequest): VerifyOtpResponse {
        return api.verifyOtp(request)
    }

    override suspend fun refreshToken(): RefreshTokenResponse {
        return api.refreshToken()
    }

    override suspend fun userInfo(): UserInfoResponse {
        return api.userInfo()
    }

    override suspend fun userInfoEdit(request: RegisterUserRequest): RegisterUserResponse {
        return api.userInfoEdit(request)
    }

    override suspend fun childInfoEdit(body: UserInfoDto): UserInfoResponse {
        return api.childInfoEdit(body)
    }
}