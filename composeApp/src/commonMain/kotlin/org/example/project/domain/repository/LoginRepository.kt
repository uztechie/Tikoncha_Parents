package org.example.project.domain.repository

import io.ktor.client.statement.HttpResponse
import org.example.project.data.remote.model.RefreshTokenResponse
import org.example.project.data.remote.model.RegisterUserRequest
import org.example.project.data.remote.model.RegisterUserResponse
import org.example.project.data.remote.model.UserInfoResponse

import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.saidburxon.newedu.data.model.SendOtpResponse
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpResponse


interface LoginRepository {

    suspend fun sendOtp(request: SendOtpRequest): SendOtpResponse

    suspend fun verifyOtp(request: VerifyOtpRequest): VerifyOtpResponse

    suspend fun registerUser(request: RegisterUserRequest): RegisterUserResponse

    suspend fun refreshToken(): RefreshTokenResponse
    suspend fun userInfo(): UserInfoResponse

}