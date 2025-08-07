package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

expect object SystemBarTheme {
    @Composable
    fun apply(
        isDark: Boolean,
        statusBarColor: Color,
        navigationBarColor: Color,
        navigationBarFallbackColor: Color,
        transparentStatusBar: Boolean
    )
}