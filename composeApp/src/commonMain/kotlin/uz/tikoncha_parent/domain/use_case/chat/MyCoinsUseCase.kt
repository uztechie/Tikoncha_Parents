package uz.tikoncha_parent.domain.use_case.chat

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.no_internet_connection
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.domain.model.MyCoins
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.MyCoinsRepository

class MyCoinsUseCase(
    private val repository: MyCoinsRepository
) {
    suspend operator fun invoke(): Resource<MyCoins> {
        return try {
            Resource.Success(repository.getMyCoins())
        }
        catch (e: IOException){
            Resource.Error(
                resId = Res.string.no_internet_connection,
                cause = e
            )
        }
        catch (e: Exception) {
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }
    }
}