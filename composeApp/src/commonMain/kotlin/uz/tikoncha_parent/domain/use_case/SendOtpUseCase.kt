package uz.tikoncha_parent.domain.use_case

import okio.IOException
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.no_internet_connection
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.LoginRepository
import uz.saidburxon.newedu.data.model.SendOtpRequest


class SendOtpUseCase(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(request: SendOtpRequest): Resource<Boolean> {
        return try {
            val response = repository.sendOtp(request)
            if (response.success){
                Resource.Success(true)
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        }
        catch (e: IOException){
            Resource.Error(
                resId = Res.string.no_internet_connection,
                cause = e
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }
    }
}