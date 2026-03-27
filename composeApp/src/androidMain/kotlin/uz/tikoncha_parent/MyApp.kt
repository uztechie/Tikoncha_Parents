package uz.tikoncha_parent

import android.app.Application
import org.koin.android.ext.koin.androidContext
import uz.tikoncha_parent.core.initKoin
import uz.tikoncha_parent.platform.appContext


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
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
