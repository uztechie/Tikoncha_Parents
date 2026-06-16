package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*

enum class ThemeMode(val resId: StringResource){
    SYSTEM(resId = Res.string.theme_system),
    LIGHT(resId = Res.string.yorug),
    DARK(resId = Res.string.qorong_u),
}

@Composable
fun rememberIsDarkTheme(): Boolean {
    val themeMode by ThemeController.mode.collectAsState()
    val systemDark = isSystemInDarkTheme()

    return when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.DARK   -> true
        ThemeMode.LIGHT  -> false
    }
}