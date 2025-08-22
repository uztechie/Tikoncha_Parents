package org.example.project.data.repository

import org.example.project.data.remote.LoginApiService
import org.example.project.data.remote.model.RefreshTokenResponse
import org.example.project.data.remote.model.RegisterUserRequest
import org.example.project.data.remote.model.RegisterUserResponse
import org.example.project.data.remote.model.UserInfoResponse
import org.example.project.domain.repository.LoginRepository
import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.saidburxon.newedu.data.model.SendOtpResponse
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpResponse

class LoginRepositoryImpl(private val api: LoginApiService): LoginRepository {
    override suspend fun sendOtp(request: SendOtpRequest): SendOtpResponse {
        return api.sendOtp(request)
    }

    override suspend fun verifyOtp(request: VerifyOtpRequest): VerifyOtpResponse {
        return api.verifyOtp(request)
    }

    override suspend fun registerUser(request: RegisterUserRequest): RegisterUserResponse {
        return api.registerUser(request)
    }

    override suspend fun refreshToken(): RefreshTokenResponse {
        return api.refreshToken()
    }

    override suspend fun userInfo(): UserInfoResponse {
        return api.userInfo()
    }


}