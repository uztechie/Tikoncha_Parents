package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Stable
class ScreenSystemBarsState(val modifier: Modifier)

@Composable
fun rememberScreenSystemBars(
    statusBarColor: Color = AppColors.bg.surface,
    navigationBarColor: Color = AppColors.bg.surface,
    hasStatusBarPadding: Boolean = true,
    hasNavigationBarPadding: Boolean = true
): ScreenSystemBarsState {

    val modifier = when {
        hasStatusBarPadding && hasNavigationBarPadding ->
            Modifier
                .background(navigationBarColor)
                .navigationBarsPadding()
                .background(statusBarColor)
                .statusBarsPadding()

        hasStatusBarPadding ->
            Modifier
                .background(statusBarColor)
                .statusBarsPadding()

        hasNavigationBarPadding ->
            Modifier
                .background(navigationBarColor)
                .navigationBarsPadding()

        else -> Modifier
    }

    return ScreenSystemBarsState(modifier = modifier)

}