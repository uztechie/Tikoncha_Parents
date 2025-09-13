package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import uz.tikoncha_parent.data.mapper.ExtendedColors
import uz.tikoncha_parent.data.mapper.toDarkColorScheme
import uz.tikoncha_parent.data.mapper.toLightColorScheme
import uz.tikoncha_parent.platform.SystemBarTheme
import uz.tikoncha_parent.ui.DarkCardColors
import uz.tikoncha_parent.ui.TonalButtonContainerColor


//val LightColors = lightColorScheme(
//    secondary = md_light_hint,
//    background = md_light_background,
//    tertiary = md_light_border,
//    onBackground = md_light_text,
//    primaryContainer = md_light_card,
//    surfaceBright = md_light_button_menu,
//    surfaceContainer = md_light_button_background,
//    scrim = md_light_icon_button,
//    tertiaryContainer = md_light_tonal_button_container,
//    outline = md_light_notification,
//    primary = md_light_primary
//)
//
//val DarkColors = darkColorScheme(
//    secondary = md_dark_hint,
//    background = md_dark_background,
//    tertiary = md_dark_border,
//    onBackground = md_dark_text,
//    primaryContainer = md_dark_card,
//    surfaceBright = md_dark_button_menu,
//    surfaceContainer = md_dark_button_background,
//    scrim = md_dark_icon_button,
//    tertiaryContainer = md_dark_tonal_button_container,
//    outline = md_dark_notification,
//    primary = md_dark_primary
//)
//
//@Composable
//fun NoteMarkTheme(
//    mode: ThemeMode,
//    content: @Composable ()-> Unit
//){
//
//
//    val isDark = when (mode) {
//        ThemeMode.SYSTEM -> isSystemInDarkTheme()
//        ThemeMode.DARK   -> true
//        ThemeMode.LIGHT  -> false
//    }
//
//
//
//    val colorScheme = if (isDark){
//        DarkColors
//    }else
//        LightColors
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//    ){
//
//        val statusBar = colorScheme.background
//        val navBarTransparent = colorScheme.background
//        val navBarFallback = colorScheme.background
//
//        val cfg = LocalBarsConfig.current.value
//
//        println("Themeeeee transparent=$cfg")
//
//        SystemBarTheme.apply(
//            isDark = isDark,
//            statusBarColor = statusBar,
//            navigationBarColor = navBarTransparent,
//            navigationBarFallbackColor = navBarFallback,
//            transparentStatusBar = cfg.transparentStatusBar
//        )
//        content()
//    }
//}


val LightExtended = ExtendedColors(
    primaryColor = md_light_primary,
    primaryLightColor = md_light_hint,
    backgroundColor = md_light_background,
    cardColor = md_light_card,
    onBackgroundColor = md_light_text,
    hintColor = md_light_hint,
    textColor = md_light_text,
    borderColor = md_light_border,
    buttonMenuColor = md_light_button_menu,
    disabledColor = md_light_icon_button,
    shadowColor = md_light_notification,
    buttonColor = md_light_button_background,
    tonalButtonColor = TonalButtonContainerColor
)

val DarkExtended = ExtendedColors(
    primaryColor = md_dark_primary,
    primaryLightColor = md_dark_hint,
    backgroundColor = md_dark_background,
    cardColor = md_dark_card,
    onBackgroundColor = md_dark_text,
    hintColor = md_dark_hint,
    textColor = md_dark_text,
    borderColor = md_dark_border,
    buttonMenuColor = md_dark_button_menu,
    disabledColor = md_dark_icon_button,
    shadowColor = md_dark_notification,
    buttonColor = md_dark_button_background,
    tonalButtonColor = DarkCardColors
)


@Composable
fun TikonchaParentTheme(
    mode: ThemeMode,
    content: @Composable () -> Unit
) {
    val isDark = when (mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK   -> true
        ThemeMode.LIGHT  -> false
    }

    val extended = if (isDark) DarkExtended else LightExtended
    val colorScheme = if (isDark) toDarkColorScheme(extended) else toLightColorScheme(extended)

    MaterialTheme(colorScheme = colorScheme) {
        ProvideExtendedColors(extended) {
            val inPreview = androidx.compose.ui.platform.LocalInspectionMode.current

            if (!inPreview) {
                val cfg = LocalBarsConfig.current.value
                SystemBarTheme.apply(
                    isDark = isDark,
                    statusBarColor = colorScheme.background,
                    navigationBarColor = colorScheme.background,
                    navigationBarFallbackColor = colorScheme.background,
                    transparentStatusBar = cfg.transparentStatusBar
                )
            }
            content()
        }
    }
}
