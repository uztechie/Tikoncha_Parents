package uz.tikoncha_parent.data.remote.telegram

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.net.Uri
import org.telegram.login.TelegramLogin
import uz.tikoncha_parent.domain.model.auth.TelegramAuthResult
import java.lang.ref.WeakReference

object TelegramLoginCoordinator {

    private var appContext: Context? = null
    private var activityRef: WeakReference<Activity>? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun bindActivity(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun unbindActivity(activity: Activity) {
        if (activityRef?.get() === activity) activityRef = null
    }

    fun currentContext(): Context? = activityRef?.get() ?: appContext

    fun startLogin(): Boolean {
        val activity = activityRef?.get() ?: return false
        return try {
            TelegramLogin.startLogin(activity)
            true
        } catch (e: Throwable) {
            TelegramAuthBus.emitResult(
                TelegramAuthResult.Error(e.message ?: "Telegram login boshlanmadi")
            )
            false
        }
    }

    fun handleResponse(uri: Uri) {
        TelegramLogin.handleLoginResponse(
            uri = uri,
            onSuccess = { data ->
                TelegramAuthBus.emitResult(TelegramAuthResult.Success(data.idToken))
            },
            onError = { error ->
                TelegramAuthBus.emitResult(
                    TelegramAuthResult.Error(error.message ?: "Telegram xatosi")
                )
            }
        )
    }


}