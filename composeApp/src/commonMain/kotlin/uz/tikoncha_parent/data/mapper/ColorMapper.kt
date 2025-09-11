package uz.tikoncha_parent.data.mapper

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import uz.tikoncha_parent.ui.DarkColor
import uz.tikoncha_parent.ui.LightColor


data class ExtendedColors(
    val primaryColor: Color,
    val primaryLightColor: Color,
    val backgroundColor: Color,
    val cardColor: Color,
    val onBackgroundColor: Color,
    val hintColor: Color,
    val textColor: Color,
    val borderColor: Color,
    val buttonMenuColor: Color,
    val disabledColor: Color,
    val shadowColor: Color,
    val buttonColor: Color,
    val tonalButtonColor: Color,
)
fun toLightColorScheme(x: ExtendedColors) = lightColorScheme(
    primary = x.primaryColor,
    onPrimary = LightColor, // if Primary is dark — use light content
    secondary = x.primaryLightColor,
    onSecondary = DarkColor,
    background = x.backgroundColor,
    onBackground = x.onBackgroundColor,
    surface = x.backgroundColor,
    onSurface = x.onBackgroundColor,
    surfaceVariant = x.cardColor,
    onSurfaceVariant = x.textColor,
    outline = x.borderColor,
    error = Color(0xFFDC2626),
    onError = LightColor,
)

fun toDarkColorScheme(x: ExtendedColors) = darkColorScheme(
    primary = x.primaryColor,
    onPrimary = LightColor,
    secondary = x.primaryLightColor,
    onSecondary = DarkColor,
    background = x.backgroundColor,
    onBackground = x.onBackgroundColor,
    surface = x.backgroundColor,
    onSurface = x.onBackgroundColor,
    surfaceVariant = x.cardColor,
    onSurfaceVariant = x.textColor,
    outline = x.borderColor,
    error = Color(0xFFF87171),
    onError = DarkColor,
)