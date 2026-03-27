package uz.tikoncha_parent.platform

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

lateinit var appContext: Context

actual fun openTelegram(phoneNumber: String) {
    val context = appContext
    val phone = phoneNumber.removePrefix("+")
    val uri = Uri.parse("https://t.me/tikoncha_bot?start=$phone")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    val resolvedApps = context.packageManager
        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        .map { it.activityInfo.packageName }

    val preferredPackages = listOf(
        "org.telegram.messenger",
        "org.telegram.messenger.web",
        "org.telegram.plus",
        "com.vidiogram",
        "org.nicegram.application",
    )

    val target = preferredPackages.firstOrNull { it in resolvedApps }
        ?: resolvedApps.firstOrNull { "telegram" in it.lowercase() }

    if (target != null) {
        context.startActivity(intent.apply { setPackage(target) })
    } else {
        context.startActivity(
            Intent.createChooser(intent, null).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}