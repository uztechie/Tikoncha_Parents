package uz.tikoncha_parent.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.media_play
import tikoncha_parents.composeapp.generated.resources.pause

@Composable
fun PlayerControlsOverlay(
    modifier: Modifier = Modifier,
    state: PlayerUiState,
    event: (PlayerEvent) -> Unit,
    controlsVisible: Boolean,
    primaryColor: Color,
    showCloseButton: Boolean = true,
    onUserInteracted: () -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (showCloseButton) {
                CircleIconButton(
                    icon = { Icon(Icons.Default.Close, null, tint = Color.White) },
                    onClick = { onUserInteracted(); onClose() }
                )
            } else {
                Spacer(Modifier)
            }

            if (controlsVisible) {
                CircleIconButton(
                    icon = {
                        Icon(
                            if (state.isMuted) Icons.AutoMirrored.Default.VolumeOff
                            else Icons.AutoMirrored.Default.VolumeUp,
                            null, tint = Color.White
                        )
                    },
                    onClick = { onUserInteracted(); event(PlayerEvent.ToggleMute) }
                )
            } else {
                Spacer(Modifier)
            }
        }

        Spacer(Modifier.weight(1f))

        if (controlsVisible) {
            VideoSeekBar(
                modifier = Modifier.fillMaxWidth(),
                positionMs = state.positionMs,
                durationMs = state.durationMs,
                bufferedMs = state.bufferedMs,
                primaryColor = primaryColor,
                onSeek = { ms ->
                    onUserInteracted()
                    event(PlayerEvent.SeekTo(ms))
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimePill(formatHourMinuteSecond(state.positionMs))
                TimePill(formatHourMinuteSecond(state.durationMs))
            }

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    CircleIconButton(
                        size = 55.dp,
                        icon = { Icon(Icons.Default.Replay10, null, tint = Color.White) },
                        onClick = { onUserInteracted(); event(PlayerEvent.SeekBy(-10000L)) }
                    )
                    CircleIconButton(
                        size = 65.dp,
                        icon = {
                            Icon(
                                if (state.isPlaying) painterResource(Res.drawable.pause)
                                else painterResource(Res.drawable.media_play),
                                null, tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        },
                        onClick = { onUserInteracted(); event(PlayerEvent.TogglePlayPause) }
                    )
                    CircleIconButton(
                        size = 55.dp,
                        icon = { Icon(Icons.Default.Forward10, null, tint = Color.White) },
                        onClick = { onUserInteracted(); event(PlayerEvent.SeekBy(10000L)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    size: androidx.compose.ui.unit.Dp = 44.dp,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .background(Color.Black.copy(0.4f), CircleShape)
    ) { icon() }
}

@Composable
private fun TimePill(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 11.sp,
        modifier = Modifier
            .background(Color.Black.copy(0.2f), CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

private fun formatHourMinuteSecond(ms: Long): String {
    val totalSec = (ms / 1000).coerceAtLeast(0)
    val h = totalSec / 3600
    val m = (totalSec % 3600) / 60
    val s = totalSec % 60
    return if (h > 0) "$h:${m.pad2()}:${s.pad2()}"
    else "${m.pad2()}:${s.pad2()}"
}

private fun Long.pad2(): String = if (this < 10) "0$this" else "$this"