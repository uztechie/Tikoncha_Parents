package uz.tikoncha_parent.presentation.player

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

actual class ImmersiveController(private val activity: Activity?) {

    actual fun enter() {
        val window = activity?.window ?: return
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    actual fun exit() {
        val window = activity?.window ?: return
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView)
            .show(WindowInsetsCompat.Type.systemBars())
    }
}

@Composable
actual fun rememberImmersiveController(): ImmersiveController {
    val activity = LocalActivity.current
    return remember(activity) { ImmersiveController(activity) }
}