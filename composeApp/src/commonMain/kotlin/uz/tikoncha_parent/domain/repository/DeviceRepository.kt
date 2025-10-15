package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.data.remote.model.DeviceRegisterResponse

interface DeviceRepository {

    suspend fun registerDevice(request: DeviceRegisterRequest): DeviceRegisterResponse
}