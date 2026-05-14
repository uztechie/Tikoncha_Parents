package uz.tikoncha_parent.presentation.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import uz.tikoncha_parent.data.player.PlayerEngine

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoSurface(
    engine: PlayerEngine,
    modifier: Modifier
) {
    UIKitView(
        modifier = modifier,
        factory = { PlayerUIView(engine.avPlayer) },
        update = { /* layoutSubviews bounds'ni o'zi yangilaydi */ }
    )
}