package uz.tikoncha_parent.data.repository

import uz.tikoncha_parent.data.remote.DeviceApiService
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.data.remote.model.DeviceRegisterResponse
import uz.tikoncha_parent.domain.repository.DeviceRepository

class DeviceRepositoryImpl(
    private val api: DeviceApiService
): DeviceRepository {
    override suspend fun registerDevice(request: DeviceRegisterRequest): DeviceRegisterResponse {
        return api.registerDevice(request)
    }
}