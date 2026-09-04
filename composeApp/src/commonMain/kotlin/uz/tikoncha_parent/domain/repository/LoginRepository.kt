package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.RegisterUserRequest
import uz.tikoncha_parent.data.remote.model.UserInfoDto
import uz.tikoncha_parent.domain.model.app_error.Outcome

interface LoginRepository {

    suspend fun userInfo(): Outcome<UserInfoDto>
    suspend fun userInfoEdit(request: RegisterUserRequest): Outcome<UserInfoDto>
    suspend fun childInfoEdit(body: UserInfoDto): Outcome<UserInfoDto>
}