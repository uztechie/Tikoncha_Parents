// composeApp/src/androidMain/kotlin/uz/tikoncha_parent/data/service/AndroidNotificationHelper.kt
package uz.tikoncha_parent.data.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uz.tikoncha_parent.R

object AndroidNotificationHelper {

    private const val CHANNEL_ID = "general_notifications"
    private const val CHANNEL_NAME = "General notifications"
    private const val CHANNEL_DESC = "App messages and alerts"

    fun ensureChannel(ctx: Context, importanceHigh: Boolean = true) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(CHANNEL_ID) != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            if (importanceHigh) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = CHANNEL_DESC
            enableVibration(true)
            setShowBadge(true)
        }
        nm.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun show(
        ctx: Context,
        title: String?,
        body: String?,
        pendingIntent: PendingIntent? = null,
        notificationId: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ActivityCompat.checkSelfPermission(
                ctx, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }
        if (!NotificationManagerCompat.from(ctx).areNotificationsEnabled()) return

        ensureChannel(ctx)

        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.launcher_white)
            .setContentTitle(title ?: "New message")
            .setContentText(body ?: "")
            .setStyle(NotificationCompat.BigTextStyle().bigText(body ?: ""))
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .apply { if (pendingIntent != null) setContentIntent(pendingIntent) }

        NotificationManagerCompat.from(ctx).notify(notificationId, builder.build())
    }
}