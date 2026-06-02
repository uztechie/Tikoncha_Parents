package uz.tikoncha_parent.data.remote.telegram

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri


actual fun isTelegramAppInstalled(): Boolean {
    val ctx = TelegramLoginCoordinator.currentContext() ?: return false
    val intent = Intent(Intent.ACTION_VIEW, "tg://resolve".toUri())
    return intent.resolveActivity(ctx.packageManager) != null
}