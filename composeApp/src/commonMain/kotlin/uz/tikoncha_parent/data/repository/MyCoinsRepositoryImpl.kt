package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.toDomain
import uz.tikoncha_parent.data.mapper.todo.toDomain
import uz.tikoncha_parent.data.remote.MyCoinsApiService
import uz.tikoncha_parent.domain.model.MyCoins
import uz.tikoncha_parent.domain.repository.MyCoinsRepository

class MyCoinsRepositoryImpl(
    private val api: MyCoinsApiService
) : MyCoinsRepository {
    override suspend fun getMyCoins(): MyCoins {
        val response = api.getMyCoins()
        if (response.success && response.data != null) return response.data.toDomain()
        error(response.error ?: "MyCoins error (code=${response.code})")
    }
}