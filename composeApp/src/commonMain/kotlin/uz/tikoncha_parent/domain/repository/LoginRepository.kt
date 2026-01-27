package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.RefreshTokenResponse
import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.data.remote.model.RegisterUserResponse
import uz.tikoncha_parent.data.remote.model.UserInfoResponse

import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.saidburxon.newedu.data.model.SendOtpResponse
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpResponse
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.UserInfo


interface LoginRepository {

    suspend fun sendOtp(request: SendOtpRequest): SendOtpResponse

    suspend fun verifyOtp(request: VerifyOtpRequest): VerifyOtpResponse

    suspend fun registerUser(request: RegisterUserRequest): RegisterUserResponse

    suspend fun refreshToken(): RefreshTokenResponse
    suspend fun userInfo(): UserInfoResponse
    suspend fun userInfoEdit(request: RegisterUserRequest): RegisterUserResponse

    suspend fun childInfoEdit(body: UserInfoDto): UserInfoResponse
}