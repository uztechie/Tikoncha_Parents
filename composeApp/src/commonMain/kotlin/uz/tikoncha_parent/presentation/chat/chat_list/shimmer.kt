package uz.tikoncha_parent.presentation.chat.chat_list

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

/**
 * Shimmer loading effect modifier.
 * KMP (Android + iOS) uchun ishlaydi — faqat sof Compose API.
 *
 * Misol:
 * Box(Modifier.size(50.dp).clip(CircleShape).shimmer())
 */
fun Modifier.shimmer(
    colors: List<Color> = defaultShimmerColors,
    durationMillis: Int = 1200,
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2f * size.width,
        targetValue = 2f * size.width,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer-offset",
    )

    this
        .onGloballyPositioned { size = it.size }
        .background(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(startOffsetX, 0f),
                end = Offset(
                    startOffsetX + size.width.toFloat(),
                    size.height.toFloat()
                ),
            )
        )
}

/**
 * Default shimmer ranglari (light va dark theme uchun umumiy).
 * Agar theme'ga mos qilmoqchi bo'lsangiz:
 *   Modifier.shimmer(colors = listOf(MaterialTheme.colorScheme.surface, ...))
 */
private val defaultShimmerColors: List<Color> = listOf(
    Color(0xFFB8B5B5).copy(alpha = 0.3f),
    Color(0xFFB8B5B5).copy(alpha = 0.5f),
    Color(0xFFB8B5B5).copy(alpha = 0.3f),
)