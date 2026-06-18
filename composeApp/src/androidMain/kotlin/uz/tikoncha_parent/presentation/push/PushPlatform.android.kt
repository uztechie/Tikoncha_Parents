// composeApp/src/androidMain/kotlin/uz/tikoncha_parent/presentation/push/PushPlatform.android.kt
package uz.tikoncha_parent.presentation.push

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import uz.tikoncha_parent.data.service.AndroidNotificationHelper
import java.lang.ref.WeakReference

actual object PushPlatform {
    private var activityRef: WeakReference<Activity>? = null
    private var appContext: Context? = null

    @JvmStatic
    fun bind(activity: Activity) {
        activityRef = WeakReference(activity)
        appContext = activity.applicationContext
    }

    actual fun initialize() {
        val ctx = appContext ?: activityRef?.get() ?: return
        AndroidNotificationHelper.ensureChannel(ctx)
    }

    actual fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val act = activityRef?.get() ?: return
        if (NotificationManagerCompat.from(act).areNotificationsEnabled()) return
        ActivityCompat.requestPermissions(
            act,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            1001
        )
    }
}