package uz.tikoncha_parent

import android.app.Application
import org.koin.android.ext.koin.androidContext
import uz.tikoncha_parent.core.initKoin


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