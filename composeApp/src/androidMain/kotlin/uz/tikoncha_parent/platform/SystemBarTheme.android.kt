package uz.tikoncha_parent.platform

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

actual object SystemBarTheme {
    @Composable
    actual fun applyIconStyle(isDark: Boolean) {
        val view = LocalView.current
        if (view.isInEditMode) return

        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
    }
}