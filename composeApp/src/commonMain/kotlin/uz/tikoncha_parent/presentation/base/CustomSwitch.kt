
package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,

    // O‘lchamlar
    width: Dp = 48.dp,
    height: Dp = 28.dp,
    padding: Dp = 3.dp, // track ichidagi bo'shliq

    // Ranglar
    trackOnColor: Color = MaterialTheme.colorScheme.primary,
    trackOffColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    trackBorderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),

    thumbOnColor: Color = MaterialTheme.colorScheme.onPrimary,
    thumbOffColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val trackShape = RoundedCornerShape(percent = 50)
    val thumbSize = height - padding * 2

    val trackColor by animateColorAsState(
        targetValue = if (checked) trackOnColor else trackOffColor,
        label = "trackColor"
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) thumbOnColor else thumbOffColor,
        label = "thumbColor"
    )

    // Thumb gorizontal ofseti
    val maxOffset = (width - thumbSize - padding * 2)
    val targetOffset = if (checked) maxOffset else 0.dp
    val animatedOffset by animateDpAsState(targetValue = targetOffset, label = "thumbOffset")

    Box(
        modifier = modifier
            .size(width, height)
            .clip(trackShape)
            .background(trackColor, trackShape)
            .let {
                if (!checked) it.border(1.dp, trackBorderColor, trackShape) else it
            }
            .semantics(mergeDescendants = true) {
                toggleableState = if (checked) androidx.compose.ui.state.ToggleableState.On
                else androidx.compose.ui.state.ToggleableState.Off
            }
            .clickable(
                enabled = enabled,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Thumb
        Box(
            modifier = Modifier
                .padding(start = padding, top = padding, bottom = padding)
                .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}
