package uz.tikoncha_parent.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

expect object SystemBarTheme {
    @Composable
    fun applyIconStyle(isDark: Boolean)
}