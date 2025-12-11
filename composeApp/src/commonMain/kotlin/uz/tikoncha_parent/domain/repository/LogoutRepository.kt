package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.logout.LogoutResponse

interface LogoutRepository {
    suspend fun getLogout(): LogoutResponse
}