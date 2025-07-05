package org.example.project.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.presentation.base.theme.*

@Composable
fun CustomLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.1f),
    height: Dp = 6.dp
) {

    val color = when(progress){
        in 0f .. 0.19f -> ProgressColor1
        in 0.2f..0.39f -> ProgressColor2
        in 0.4f..0.59f -> ProgressColor3
        in 0.2f..0.79f -> ProgressColor4
        else -> ProgressColor5
    }


    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth().
            clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .background(color)
        )
    }
}