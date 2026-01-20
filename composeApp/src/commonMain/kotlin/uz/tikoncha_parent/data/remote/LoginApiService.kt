package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import uz.tikoncha_parent.data.local.AppSettings
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


    suspend fun childInfoEdit(body: UserInfoDto): UserInfoResponse =
        client.safeRequest(
            method = HttpMethod.Patch,
            url = "/users/student-info",
            block = {
                setBody(body)
            }
        )
}