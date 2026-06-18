package uz.tikoncha_parent.presentation.push

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import uz.tikoncha_parent.common.AppCode
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.domain.use_case.device.RegisterDeviceUseCase
import uz.tikoncha_parent.platform.getDeviceInfo

object FcmTokenRegister {
    private val scope = CoroutineScope(SupervisorJob()+ Dispatchers.Default)

    fun submit(fcmToken: String){
        val di = getDeviceInfo()
        val request = DeviceRegisterRequest(
            manufacturer = di.manufacturer,
            model_name = di.modelName,
            os = di.os,
            os_version = di.osVersion,
            fcm_token = fcmToken,
            app_code = AppCode.currentAppCode
        )

        val useCase: RegisterDeviceUseCase = KoinPlatform.getKoin().get()
        scope.launch { runCatching { useCase.invoke(request) } }

    }
}