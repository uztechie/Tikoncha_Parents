package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.data.remote.model.GetRulesData
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.DeviceRepository

class RegisterDeviceUseCase(
    private val repository: DeviceRepository
) {

    suspend operator fun invoke(request: DeviceRegisterRequest): Resource<Boolean>{
        return try {
            val response = repository.registerDevice(request)
            if (response.success){
                Resource.Success(true)
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }

    }
}