package uz.tikoncha_parent

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.telegram.login.TelegramLogin
import uz.tikoncha_parent.core.initKoin
import uz.tikoncha_parent.data.remote.telegram.TelegramConfig
import uz.tikoncha_parent.data.remote.telegram.TelegramLoginCoordinator
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

        TelegramLoginCoordinator.init(this)
        TelegramLogin.init(
            clientId = TelegramConfig.CLIENT_ID,
            redirectUri = "https://${TelegramConfig.redirectHost}/tglogin",
            scopes = TelegramConfig.SCOPES,
        )

    }
}

object AppHolder{
    lateinit var app: Application
}
