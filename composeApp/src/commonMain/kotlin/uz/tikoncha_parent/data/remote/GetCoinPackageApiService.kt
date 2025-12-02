package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import uz.tikoncha_parent.data.remote.model.CoinPackagesResponse
import uz.tikoncha_parent.data.repository.GetCoinPackageRepositoryImpl

class GetCoinPackageApiService(
    private val client: HttpClient
) {
    suspend fun getCoinPackages(): CoinPackagesResponse {
        return client.get(
            "/payments/packages"
        ).body()
    }
}