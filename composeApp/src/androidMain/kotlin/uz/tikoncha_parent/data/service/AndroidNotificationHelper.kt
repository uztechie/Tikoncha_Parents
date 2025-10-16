package uz.tikoncha_parent.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uz.tikoncha_parent.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AndroidNotificationHelper {

    private const val CHANNEL_ID = "general_notifications"
    private const val CHANNEL_NAME = "General notifications"
    private const val CHANNEL_DESC = "App messages and alerts"

    /** Kanallar faqat Android 8.0+ da kerak bo‘ladi */
    fun ensureChannel(ctx: Context, importanceHigh: Boolean = true) {
        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val exist = nm.getNotificationChannel(CHANNEL_ID)
        if (exist != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            if (importanceHigh) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESC
            enableVibration(true)
            setShowBadge(true)
        }
        nm.createNotificationChannel(channel)
    }

    /**
     * Oddiy xabar ko‘rsatish (BigText/BigPicture, optional PendingIntent, fullScreen).
     *
     * @param bigPicture agar null bo‘lsa BigText ishlatiladi
     * @param pendingIntent bosilganda ochiladigan Intent (masalan, deeplink)
     * @param asFullScreen true bo‘lsa, high-priority full-screen intent (zudlik bilan ko‘rinadi)
     * @param highImportance kanal/notification’ga yuqori priotitet beradi
     */
    fun show(
        ctx: Context,
        title: String?,
        body: String?,
        bigPicture: Bitmap? = null,
        pendingIntent: PendingIntent? = null,
        asFullScreen: Boolean = false,
        highImportance: Boolean = true,
        notificationId: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    ) {
        // Android 13+: permission tekshiruvi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ActivityCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        // Foydalanuvchi notifikatsiyalarni o‘chirgan bo‘lishi mumkin
        if (!NotificationManagerCompat.from(ctx).areNotificationsEnabled()) return

        ensureChannel(ctx, importanceHigh = highImportance)

        val style = if (bigPicture != null) {
            NotificationCompat.BigPictureStyle()
                .bigPicture(bigPicture)
                .setSummaryText(body ?: "")
        } else {
            NotificationCompat.BigTextStyle().bigText(body ?: "")
        }

        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification) // monoxrom kichik ikon (vector bo‘lsin)
            .setContentTitle(title ?: "New message")
            .setContentText(body ?: "")
            .setStyle(style)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(
                if (highImportance) NotificationCompat.PRIORITY_HIGH
                else NotificationCompat.PRIORITY_DEFAULT
            )
            .apply {
                // Bosilganda ochiladigan ekran
                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
                // Full-screen (masalan, juda muhim TODO yoki qo‘ng‘iroq)
                if (asFullScreen && pendingIntent != null) {
                    setFullScreenIntent(pendingIntent, true)
                }
            }

        NotificationManagerCompat.from(ctx).notify(notificationId, builder.build())
    }
}
