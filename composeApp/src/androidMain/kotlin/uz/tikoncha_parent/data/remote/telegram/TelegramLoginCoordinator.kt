package uz.tikoncha_parent.data.remote.telegram

import android.app.Activity
import android.net.Uri
import kotlinx.coroutines.suspendCancellableCoroutine
import org.telegram.login.TelegramLogin
import java.lang.ref.WeakReference
import kotlin.coroutines.resume

object TelegramLoginCoordinator {

    // O'zgartirish: WeakReference orqali Activity'ni ushlash
    private var activityRef: WeakReference<Activity>? = null
    private var pendingCallback: ((TelegramAuthResult) -> Unit)? = null

    fun bindActivity(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun unbindActivity(activity: Activity) {
        if (activityRef?.get() === activity) {
            activityRef = null
        }
    }

    suspend fun startLogin(): TelegramAuthResult = suspendCancellableCoroutine { cont ->
        val activity = activityRef?.get()
        if (activity == null) {
            cont.resume(TelegramAuthResult.Error("Activity mavjud emas"))
            return@suspendCancellableCoroutine
        }

        pendingCallback = { result ->
            if (cont.isActive) cont.resume(result)
        }

        try {
            TelegramLogin.startLogin(activity)
        } catch (e: Throwable) {
            pendingCallback = null
            cont.resume(TelegramAuthResult.Error(e.message ?: "Telegram login boshlanmadi"))
        }

        cont.invokeOnCancellation {
            pendingCallback = null
        }
    }

    fun handleResponse(uri: Uri) {
        TelegramLogin.handleLoginResponse(
            uri = uri,
            onSuccess = { data ->
                val cb = pendingCallback
                pendingCallback = null
                cb?.invoke(TelegramAuthResult.Success(data.idToken))
            },
            onError = { error ->
                val cb = pendingCallback
                pendingCallback = null
                cb?.invoke(TelegramAuthResult.Error(error.message ?: "Telegram xatosi"))
            }
        )
    }
}