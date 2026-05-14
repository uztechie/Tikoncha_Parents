package uz.tikoncha_parent.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun VideoSeekBar(
    modifier: Modifier = Modifier,
    positionMs: Long,
    durationMs: Long,
    bufferedMs: Long,
    primaryColor: Color,
    onSeek: (Long) -> Unit
) {
    val safeDur = durationMs.takeIf { it > 0 } ?: 1L
    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(0f) }

    val progress = if (isDragging) dragProgress
    else (positionMs.toFloat() / safeDur).coerceIn(0f, 1f)
    val buffered = (bufferedMs.toFloat() / safeDur).coerceIn(0f, 1f)

    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxWidth()
            .height(22.dp)
            .pointerInput(safeDur) {
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        dragProgress = (offset.x / size.width).coerceIn(0f, 1f)
                    },
                    onDragEnd = {
                        onSeek((dragProgress * safeDur).toLong())
                        isDragging = false
                    },
                    onHorizontalDrag = { change, _ ->
                        dragProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                    }
                )
            }
            .pointerInput(safeDur) {
                detectTapGestures { tap ->
                    val p = (tap.x / size.width).coerceIn(0f, 1f)
                    onSeek((p * safeDur).toLong())
                }
            }
            .progressSemantics(progress)
    ) {
        androidx.compose.foundation.layout.Box(
            Modifier.fillMaxWidth().height(8.dp).align(Alignment.Center)
                .background(Color(0x55FFFFFF), RoundedCornerShape(4.dp))
        )
        androidx.compose.foundation.layout.Box(
            Modifier.fillMaxWidth(buffered).height(8.dp).align(Alignment.CenterStart)
                .background(primaryColor.copy(0.3f), RoundedCornerShape(4.dp))
        )
        androidx.compose.foundation.layout.Box(
            Modifier.fillMaxWidth(progress).height(8.dp).align(Alignment.CenterStart)
                .background(primaryColor, RoundedCornerShape(4.dp))
        )
    }
}