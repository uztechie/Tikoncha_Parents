package uz.tikoncha_parent.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BarConfig(
    val paddingEnabled: Boolean = true,
    val transparentStatusBar: Boolean = false,
    val statusBarColor: Color? = null,
    val navigationBarColor: Color? = null
)

val LocalBarsConfig = staticCompositionLocalOf<MutableState<BarConfig>> {
    error("LocalBarsConfig not provided")
}