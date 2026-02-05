package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import tikoncha_parents.composeapp.generated.resources.kutilmagan_xatolik_qayta_urining
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.mapper.toDomain
import uz.tikoncha_parent.domain.model.CoinPackage
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.CoinPackageRepository

class GetCoinPackagesUseCase(
    private val repository: CoinPackageRepository
) {
    suspend operator fun invoke(): Resource<List<CoinPackage>> {
        return try {
            val response = repository.getCoinPackages()

            if (!response.success){
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
            else {
                val data = response.data.map { it.toDomain() }
                Resource.Success(data)
            }
        } catch (e: IOException) {
            Resource.Error(
                resId = Res.string.iltimos_internetga_ulang,
                cause = e
            )
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "Unknown error",
                resId = Res.string.kutilmagan_xatolik_qayta_urining
            )
        }
    }
}