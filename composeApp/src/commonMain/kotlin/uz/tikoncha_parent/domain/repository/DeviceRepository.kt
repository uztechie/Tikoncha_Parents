package uz.tikoncha_parent.domain.repository

import uz.tikoncha_parent.data.remote.device.LogoutResponse
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.data.remote.model.DeviceRegisterResponse

interface DeviceRepository {

    suspend fun registerDevice(request: DeviceRegisterRequest): DeviceRegisterResponse
    suspend fun logout(fcmToken: String): LogoutResponse
}