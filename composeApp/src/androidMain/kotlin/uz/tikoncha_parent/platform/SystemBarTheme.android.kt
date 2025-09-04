package uz.tikoncha_parent.platform

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

actual object SystemBarTheme {
    @Composable
    actual fun apply(
        isDark: Boolean,
        statusBarColor: Color,
        navigationBarColor: Color,
        navigationBarFallbackColor: Color,
        transparentStatusBar: Boolean
    ) {
        val view = LocalView.current
        if (view.isInEditMode) return

        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val darkIcons = isDark

        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !darkIcons
            isAppearanceLightNavigationBars = !darkIcons
        }


        Log.d("TAG", "applyyyyy: $transparentStatusBar")

        if (!transparentStatusBar){
            Box(Modifier.fillMaxSize()) {
                // Top inset (status bar)
                Spacer(
                    Modifier
                        .background(statusBarColor)
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                )
                // Bottom inset (nav bar)
                Spacer(
                    Modifier
                        .background(statusBarColor)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                )
            }
        }

    }
}