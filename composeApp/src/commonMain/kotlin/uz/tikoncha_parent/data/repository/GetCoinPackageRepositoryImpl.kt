package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.GetCoinPackageApiService
import uz.tikoncha_parent.data.remote.model.CoinPackagesResponse
import uz.tikoncha_parent.domain.repository.CoinPackageRepository
import uz.tikoncha_parent.domain.use_case.GetCoinPackagesUseCase

class GetCoinPackageRepositoryImpl(
    private val api: GetCoinPackageApiService
): CoinPackageRepository {
    override suspend fun getCoinPackages(): CoinPackagesResponse {
        return api.getCoinPackages()
    }
}