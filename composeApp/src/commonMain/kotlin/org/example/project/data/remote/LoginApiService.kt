package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import org.example.project.data.local.AppSettings
import org.example.project.data.remote.model.RefreshTokenResponse
import org.example.project.data.remote.model.RegisterUserRequest
import org.example.project.data.remote.model.RegisterUserResponse
import org.example.project.data.remote.model.UserInfoResponse
import uz.saidburxon.newedu.data.model.SendOtpRequest
import uz.saidburxon.newedu.data.model.SendOtpResponse
import uz.saidburxon.newedu.data.model.VerifyOtpRequest
import uz.saidburxon.newedu.data.model.VerifyOtpResponse

class LoginApiService(private val client: HttpClient) {


    suspend fun sendOtp(request: SendOtpRequest): SendOtpResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "auth/send-otp",
            block = {
                setBody(request)
            }
        )


    suspend fun verifyOtp(request: VerifyOtpRequest): VerifyOtpResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "auth/verify-otp",
            block = {
                setBody(request)
            }
        )

    suspend fun registerUser(request: RegisterUserRequest): RegisterUserResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "users/parent-info",
            block = {
                setBody(request)
            }
        )

    suspend fun refreshToken(): RefreshTokenResponse =
        client.safeRequest(
            method = HttpMethod.Post,
            url = "auth/refresh",
            block = {
                parameter("refresh_token", AppSettings.refreshToken)
            }
        )

    suspend fun userInfo(): UserInfoResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "users/parent-info",
            block = {
                parameter("user_id", AppSettings.userId)
            }
        )



}