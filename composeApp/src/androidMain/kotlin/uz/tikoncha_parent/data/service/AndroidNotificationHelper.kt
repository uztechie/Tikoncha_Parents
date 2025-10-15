package uz.tikoncha_parent.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uz.tikoncha_parent.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AndroidNotificationHelper {
    private const val CHANNEL_ID = "general channel"

    fun ensureChannel(ctx: Context){
        val ch = NotificationChannel(CHANNEL_ID, "General", NotificationManager.IMPORTANCE_DEFAULT)
        (ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(ch)
    }

    fun show(ctx: Context, title: String?, body: String?) {
        ensureChannel(ctx)


        val n = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification) // o'zingizning ikona
            .setContentTitle(title ?: "New message")
            .setContentText(body)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(ctx).notify(System.currentTimeMillis().toInt(), n)
    }
}