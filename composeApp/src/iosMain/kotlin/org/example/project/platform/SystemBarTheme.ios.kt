package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

actual object SystemBarTheme {
    @Composable
    actual fun apply(
        isDark: Boolean,
        statusBarColor: Color,
        navigationBarColor: Color,
        navigationBarFallbackColor: Color
    ) {
    }
}