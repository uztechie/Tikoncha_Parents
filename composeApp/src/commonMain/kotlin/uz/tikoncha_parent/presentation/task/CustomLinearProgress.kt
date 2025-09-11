package uz.tikoncha_parent.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.ProgressColor1
import uz.tikoncha_parent.ui.ProgressColor2
import uz.tikoncha_parent.ui.ProgressColor3
import uz.tikoncha_parent.ui.ProgressColor4
import uz.tikoncha_parent.ui.ProgressColor5
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.extendedColor.onBackgroundColor.copy(alpha = 0.1f),
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