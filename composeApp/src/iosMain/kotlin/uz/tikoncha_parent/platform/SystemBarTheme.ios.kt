package uz.tikoncha_parent.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

actual object SystemBarTheme {
    @Composable
    actual fun applyIconStyle(isDark: Boolean) {
        SideEffect {
            val style = if (isDark) UIStatusBarStyleLightContent else UIStatusBarStyleDarkContent
            UIApplication.sharedApplication.setStatusBarStyle(style)
        }
    }
}