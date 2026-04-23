package uz.tikoncha_parent.platform

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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

actual object SystemBarTheme {
    @Composable
    actual fun apply(
        isDark: Boolean,
        statusBarColor: Color,
        navigationBarColor: Color,
        navigationBarFallbackColor: Color,
        transparentStatusBar: Boolean
    ) {
        SideEffect {
            val style =
                if (isDark) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent
            UIApplication.sharedApplication.setStatusBarStyle(style)
        }

        if (!transparentStatusBar) {
            Box(Modifier.fillMaxSize()) {
                Spacer(
                    Modifier
                        .background(statusBarColor)       // ← statusbar rangi
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                )
                Spacer(
                    Modifier
                        .background(navigationBarColor)   // ← navbar rangi (tuzatildi)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                )
            }
        }
    }
}