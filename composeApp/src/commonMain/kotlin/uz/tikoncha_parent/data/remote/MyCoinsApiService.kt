package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import uz.tikoncha_parent.data.remote.model.MyCoinsResponse

class MyCoinsApiService(
    private val client: HttpClient
) {
    suspend fun getMyCoins(): MyCoinsResponse =
        client.get("users/my-coins").body()
}