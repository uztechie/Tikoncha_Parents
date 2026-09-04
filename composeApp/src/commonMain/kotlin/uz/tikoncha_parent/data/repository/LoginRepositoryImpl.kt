package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.LoginApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.LoginRepository

class LoginRepositoryImpl(private val api: LoginApiService) : LoginRepository {

    override suspend fun userInfo(): Outcome<UserInfoDto> = apiCall(TAG) {
        val r = api.userInfo()
        val data = r.data
        if (r.success && data != null) Outcome.Success(data)
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
    }

    override suspend fun userInfoEdit(request: RegisterUserRequest): Outcome<UserInfoDto> = apiCall(TAG) {
        val r = api.userInfoEdit(request)
        val data = r.data
        if (r.success && data != null) Outcome.Success(data)
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
    }

    override suspend fun childInfoEdit(body: UserInfoDto): Outcome<UserInfoDto> = apiCall(TAG) {
        val r = api.childInfoEdit(body)
        val data = r.data
        if (r.success && data != null) Outcome.Success(data)
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
    }

    private companion object { const val TAG = "LoginRepository" }
}