package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import uz.tikoncha_parent.data.mapper.ExtendedColors
import uz.tikoncha_parent.platform.SystemBarTheme
import uz.tikoncha_parent.ui.Background
import uz.tikoncha_parent.ui.BackgroundColor
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.CardColors
import uz.tikoncha_parent.ui.ColorWhite
import uz.tikoncha_parent.ui.DarkBackgroundColor
import uz.tikoncha_parent.ui.DarkButtonBackgroundColors
import uz.tikoncha_parent.ui.DarkCardColors
import uz.tikoncha_parent.ui.DarkCardColorsButton
import uz.tikoncha_parent.ui.DarkColor
import uz.tikoncha_parent.ui.DarkGrayColor
import uz.tikoncha_parent.ui.DarkTextColor
import uz.tikoncha_parent.ui.DisableButtonColor
import uz.tikoncha_parent.ui.DisabledBgDark
import uz.tikoncha_parent.ui.DisabledBgLight
import uz.tikoncha_parent.ui.DisabledContentDark
import uz.tikoncha_parent.ui.DisabledContentLight
import uz.tikoncha_parent.ui.GrayColor
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.LightColor
import uz.tikoncha_parent.ui.LightGrayColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.PrimaryGraColor
import uz.tikoncha_parent.ui.PrimaryLightColor
import uz.tikoncha_parent.ui.ShadowColor
import uz.tikoncha_parent.ui.TextColor
import uz.tikoncha_parent.ui.TonalButtonContainerColor



private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = PrimaryColor,
    tertiary = PrimaryColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = PrimaryColor,
    tertiary = PrimaryColor
)


private val LightExtendedColorScheme = ExtendedColors(
    primaryColor = PrimaryColor,
    primaryLightColor = HintTextColor,
    backgroundColor = BackgroundColor,
    cardColor = CardColors,
    onBackgroundColor = TextColor,
    hintColor = HintTextColor,
    textColor = TextColor,
    borderColor = BorderColor,
    buttonMenuColor = TextColor,
    shadowColor = ShadowColor,
    shadowLightColor = Color.White,
    buttonColor = CardColors,
    tonalButtonColor = TonalButtonContainerColor,
    disabledBgColor = DisabledBgLight,
    disabledContentColor = DisabledContentLight

)
private val DarkExtendedColorScheme = ExtendedColors(
    primaryColor = PrimaryColor,
    primaryLightColor = HintTextColor,
    backgroundColor = DarkBackgroundColor,
    cardColor = DarkCardColors,
    onBackgroundColor = DarkTextColor,
    hintColor = HintTextColor,
    textColor = DarkTextColor,
    borderColor = DarkGrayColor,
    buttonMenuColor = HintTextColor,
    shadowColor = PrimaryGraColor,
    shadowLightColor = PrimaryColor.copy(alpha = 0.1f),
    buttonColor = DarkButtonBackgroundColors,
    tonalButtonColor = DarkCardColors,
    disabledBgColor = DisabledBgDark,
    disabledContentColor = DisabledContentDark
)

val MaterialTheme.extendedColor: ExtendedColors
    @Composable get() = LocalExtendedColors.current


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

    val colorScheme = when {
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColor = if (isDark) DarkExtendedColorScheme else LightExtendedColorScheme



    MaterialTheme(colorScheme = colorScheme) {
        ProvideExtendedColors(extendedColor) {
            val inPreview = androidx.compose.ui.platform.LocalInspectionMode.current

            if (!inPreview) {
                val cfg = LocalBarsConfig.current.value
                SystemBarTheme.apply(
                    isDark = isDark,
                    statusBarColor = extendedColor.backgroundColor,
                    navigationBarColor = extendedColor.backgroundColor,
                    navigationBarFallbackColor = extendedColor.backgroundColor,
                    transparentStatusBar = cfg.transparentStatusBar
                )
            }
            content()
        }
    }
}
