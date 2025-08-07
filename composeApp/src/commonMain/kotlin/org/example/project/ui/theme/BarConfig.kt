package org.example.project.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.staticCompositionLocalOf

data class BarConfig(
    val paddingEnabled: Boolean = true,
    val transparentStatusBar: Boolean = false
)

val LocalBarsConfig = staticCompositionLocalOf<MutableState<BarConfig>> {
    error("LocalBarsConfig not provided")
}