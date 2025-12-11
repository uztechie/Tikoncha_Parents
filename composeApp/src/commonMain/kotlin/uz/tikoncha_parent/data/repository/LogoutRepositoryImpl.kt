package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.LogoutApiService
import uz.tikoncha_parent.data.remote.model.logout.LogoutResponse
import uz.tikoncha_parent.domain.repository.LogoutRepository

class LogoutRepositoryImpl(
    private val api: LogoutApiService
): LogoutRepository {
    override suspend fun getLogout(): LogoutResponse {
        return api.getLogout()
    }
}