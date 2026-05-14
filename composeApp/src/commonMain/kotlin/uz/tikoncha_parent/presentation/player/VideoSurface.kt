package uz.tikoncha_parent.presentation.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uz.tikoncha_parent.data.player.PlayerEngine

@Composable
expect fun VideoSurface(
    engine: PlayerEngine,
    modifier: Modifier = Modifier
)