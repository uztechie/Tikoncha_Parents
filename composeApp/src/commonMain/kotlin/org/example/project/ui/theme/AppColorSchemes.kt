package org.example.project.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


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
    content: @Composable ()-> Unit
){

    val theme = if (isSystemInDarkTheme()){
        DarkColors
    }else
        LightColors
    MaterialTheme(
        colorScheme = theme,
        content = content
    )
}
