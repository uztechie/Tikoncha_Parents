package org.example.project.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.example.project.platform.SystemBarTheme


val LightColors = lightColorScheme(
    secondary = md_light_hint,
    background = md_light_background,
    tertiary = md_light_border,
    onBackground = md_light_text,
    primaryContainer = md_light_card,
    surfaceBright = md_light_button_menu,
    surfaceContainer = md_light_button_background,
    scrim = md_light_icon_button,
    tertiaryContainer = md_light_tonal_button_container,
    outline = md_light_notification,
    primary = md_light_primary
)

val DarkColors = darkColorScheme(
    secondary = md_dark_hint,
    background = md_dark_background,
    tertiary = md_dark_border,
    onBackground = md_dark_text,
    primaryContainer = md_dark_card,
    surfaceBright = md_dark_button_menu,
    surfaceContainer = md_dark_button_background,
    scrim = md_dark_icon_button,
    tertiaryContainer = md_dark_tonal_button_container,
    outline = md_dark_notification,
    primary = md_dark_primary
)

@Composable
fun NoteMarkTheme(
    mode: ThemeMode,
    content: @Composable ()-> Unit
){


    val isDark = when (mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK   -> true
        ThemeMode.LIGHT  -> false
    }



    val colorScheme = if (isDark){
        DarkColors
    }else
        LightColors

    MaterialTheme(
        colorScheme = colorScheme,
    ){

        val statusBar = colorScheme.background
        val navBarTransparent = colorScheme.background
        val navBarFallback = colorScheme.background

        SystemBarTheme.apply(
            isDark = isDark,
            statusBarColor = statusBar,
            navigationBarColor = navBarTransparent,
            navigationBarFallbackColor = navBarFallback,
        )
        content()
    }
}
