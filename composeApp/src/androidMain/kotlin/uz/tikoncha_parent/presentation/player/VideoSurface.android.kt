package uz.tikoncha_parent.presentation.player

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import uz.tikoncha_parent.data.player.PlayerEngine

@OptIn(UnstableApi::class)
@Composable
actual fun VideoSurface(
    engine: PlayerEngine,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                keepScreenOn = true
                player = engine.exoPlayer
            }
        },
        update = { it.player = engine.exoPlayer }
    )
}