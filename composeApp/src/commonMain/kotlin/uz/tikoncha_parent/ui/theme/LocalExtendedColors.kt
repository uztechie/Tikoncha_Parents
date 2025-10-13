// uz/tikoncha_parent/ui/theme/ExtendedColorsProvider.kt
package uz.tikoncha_parent.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.MaterialTheme
import uz.tikoncha_parent.data.mapper.ExtendedColors

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColors> {
    error("No ExtendedColors provided")
}

@Composable
fun ProvideExtendedColors(
    colors: ExtendedColors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalExtendedColors provides colors) {
        content()
    }
}
