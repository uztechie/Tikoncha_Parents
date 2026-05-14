package uz.tikoncha_parent.presentation.player

import androidx.compose.runtime.Composable

expect class ImmersiveController {
    fun enter()
    fun exit()
}

@Composable
expect fun rememberImmersiveController(): ImmersiveController