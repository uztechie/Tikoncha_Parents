package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import uz.tikoncha_parent.data.mapper.ExtendedColors
import uz.tikoncha_parent.platform.SystemBarTheme
import uz.tikoncha_parent.ui.BackgroundColor
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.CardColors
import uz.tikoncha_parent.ui.DarkBackgroundColor
import uz.tikoncha_parent.ui.DarkButtonBackgroundColors
import uz.tikoncha_parent.ui.DarkCardColors
import uz.tikoncha_parent.ui.DarkGrayColor
import uz.tikoncha_parent.ui.DarkTextColor
import uz.tikoncha_parent.ui.DisabledBgDark
import uz.tikoncha_parent.ui.DisabledBgLight
import uz.tikoncha_parent.ui.DisabledContentDark
import uz.tikoncha_parent.ui.DisabledContentLight
import uz.tikoncha_parent.ui.EditTextBorder
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.NewBackgroundColor
import uz.tikoncha_parent.ui.PrimaryAlphaColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShadowColorDark
import uz.tikoncha_parent.ui.ShadowColorLight
import uz.tikoncha_parent.ui.TextColor
import uz.tikoncha_parent.ui.TitleDarkColor
import uz.tikoncha_parent.ui.TitleLightColor
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
    primaryAlphaColor = PrimaryColor,
    backgroundColor = NewBackgroundColor,
    cardColor = CardColors,
    onBackgroundColor = TextColor,
//    hintColor = HintTextColor,
    hintColor = TitleLightColor,
    textColor = TextColor,
    borderColor = BorderColor,
    buttonMenuColor = TextColor,
    shadowColor = ShadowColorLight,
    shadowLightColor = Color.White,
    buttonColor = CardColors,
    tonalButtonColor = TonalButtonContainerColor,
    disabledBgColor = DisabledBgLight,
    disabledContentColor = DisabledContentLight,
    titleColor = TitleLightColor
)
private val DarkExtendedColorScheme = ExtendedColors(
    primaryColor = PrimaryColor,
    primaryAlphaColor = PrimaryAlphaColor,
    backgroundColor = DarkBackgroundColor,
    cardColor = DarkCardColors,
    onBackgroundColor = DarkTextColor,
//    hintColor = HintTextColor,
    hintColor = TitleDarkColor,
    textColor = DarkTextColor,
    borderColor = EditTextBorder,
    buttonMenuColor = HintTextColor,
    shadowColor = ShadowColorDark,
    shadowLightColor = PrimaryColor.copy(alpha = 0.1f),
    buttonColor = DarkButtonBackgroundColors,
    tonalButtonColor = DarkCardColors,
    disabledBgColor = DisabledBgDark,
    disabledContentColor = DisabledContentDark,
    titleColor = TitleDarkColor
)

val MaterialTheme.extendedColor: ExtendedColors
    @Composable get() = LocalExtendedColors.current

private val TikonchaLocalExtendedColors = staticCompositionLocalOf<TikonchaExtendedColors> {
    TikonchaParentLightExtendedColors
}

private val LocalTikonchaTypography = staticCompositionLocalOf<TikonchaTypography> {
    error("TikonchaTypography not provided")
}

val AppColors @Composable get() = TikonchaLocalExtendedColors.current
val AppTypography @Composable get() = LocalTikonchaTypography.current

@Composable
fun TikonchaParentTheme(
    mode: ThemeMode = ThemeMode.DARK,
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
    val typographyPack = remember { createTypography() }

    val extendedColor = if (isDark) DarkExtendedColorScheme else LightExtendedColorScheme

    val tikonchaColors = if (isDark) {
        TikonchaParentDarkExtendedColors
    } else {
        TikonchaParentLightExtendedColors
    }


    CompositionLocalProvider(
        LocalExtendedColors provides extendedColor,
        TikonchaLocalExtendedColors provides tikonchaColors,
        LocalTikonchaTypography provides typographyPack,
    ) {
        MaterialTheme(colorScheme = colorScheme, typography = typographyPack.material3) {
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
