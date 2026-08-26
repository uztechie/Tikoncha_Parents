package uz.tikoncha_parent.presentation.push

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import uz.tikoncha_parent.domain.repository.DeviceRepository

object FcmTokenRegister {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun submit(fcmToken: String) {
        val repository: DeviceRepository = KoinPlatform.getKoin().get()
        scope.launch { repository.registerDevice(fcmToken) }
    }
}