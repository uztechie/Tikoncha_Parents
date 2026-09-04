package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.mapper.toDomain
import uz.tikoncha_parent.data.remote.MyCoinsApiService
import uz.tikoncha_parent.data.remote.app_error.ApiErrorMapper
import uz.tikoncha_parent.domain.model.MyCoins
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.MyCoinsRepository

class MyCoinsRepositoryImpl(
    private val api: MyCoinsApiService
) : MyCoinsRepository {

    override suspend fun getMyCoins(): Outcome<MyCoins> = apiCall(TAG) {
        val r = api.getMyCoins()
        val data = r.data
        if (r.success && data != null) Outcome.Success(data.toDomain())
        else Outcome.Failure(ApiErrorMapper.fromCode(r.code), r.error)
    }

    private companion object { const val TAG = "MyCoinsRepository" }
}