package uz.tikoncha_parent.platform

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.net.toUri

var appContext: Context? = null

private const val TG_BOT = "tikoncha_bot"
private const val TG_PKG = "org.telegram.messenger"
actual fun openTelegram(phoneNumber: String): Boolean {
    val context = appContext ?: return false
    val phone = phoneNumber.filter { it.isDigit() }
    if (phone.isEmpty()) return false

    // Ketma-ket urinadigan URL'lar (yuqoridan pastga):
    // 1. tg://          → Telegram app. Bir nechta variant bo'lsa,
    //                     Android avtomatik sistema chooserini ko'rsatadi.
    // 2. https://t.me/  → Telegram universal link yoki brauzer.
    // 3. market://      → Play Market ilovasi (Telegram yuklab olish).
    // 4. play.google... → Play Store web (Play Market ham yo'q bo'lsa).
    val urls = listOf(
        "tg://resolve?domain=$TG_BOT&start=$phone",
        "https://t.me/$TG_BOT?start=$phone",
        "market://details?id=$TG_PKG",
        "https://play.google.com/store/apps/details?id=$TG_PKG"
    )

    for (url in urls) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
            return true
        } catch (_: Throwable) {
            // Keyingi urinishga o'tamiz — crash yo'q
        }
    }
    return false
}

actual fun openUrl(url: String): Boolean {
    val context = appContext ?: return false
    if (url.isEmpty()) return false
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return try {
        context.startActivity(intent)
        true
    } catch (_: Throwable) {
        false
    }
}

actual fun shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, null).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    appContext?.startActivity(chooser)  // <-- o'zingizning context holder'ingiz
}