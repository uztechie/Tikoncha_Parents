package uz.tikoncha_parent

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.mp.KoinPlatform
import uz.tikoncha_parent.core.initKoin
import uz.tikoncha_parent.data.remote.model.DeviceRegisterRequest
import uz.tikoncha_parent.domain.use_case.RegisterDeviceUseCase
import uz.tikoncha_parent.platform.DeviceInfo
import uz.tikoncha_parent.platform.getDeviceInfo
import uz.tikoncha_parent.presentation.push.PushPlatform


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppHolder.app = this
        initMapKit()
        initKoin(
            config = {androidContext(this@MyApp)}
        )




    }
}

object AppHolder{
    lateinit var app: Application
}
