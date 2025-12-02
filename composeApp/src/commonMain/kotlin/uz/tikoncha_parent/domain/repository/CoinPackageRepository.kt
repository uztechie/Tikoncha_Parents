package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.CoinPackagesResponse

interface CoinPackageRepository {
    suspend fun getCoinPackages(): CoinPackagesResponse
}