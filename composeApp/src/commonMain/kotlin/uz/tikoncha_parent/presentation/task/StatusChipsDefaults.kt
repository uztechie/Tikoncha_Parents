package uz.tikoncha_parent.presentation.task

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

object StatusChipsDefaults {
    val HorizontalPadding: Dp = 22.dp
    val VerticalPadding: Dp = 12.dp
    val Spacing: Dp = 8.dp
    val Elevation: Dp = 2.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusChips(
    items: List<Pair<TaskFilterChip, String>>,
    selected: TaskFilterChip?,
    onChipClick: (TaskFilterChip) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(2.dp),
    spacing: Dp = StatusChipsDefaults.Spacing,
    activeColor: Color = AppColors.bg.primary,
    inactiveColor: Color = AppColors.bg.surface,
    activeTextColor: Color = AppColors.text.inverse,
    inactiveTextColor: Color = AppColors.text.primary,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items(
            items = items,
            key = { it.first },              // ✅ enum (stable) key
        ) { (chip, label) ->                 // ✅ destructuring
            StatusChip(
                label = label,               // ✅ String
                isSelected = chip == selected,
                onClick = { onChipClick(chip) }, // ✅ toggle ViewModel da
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    activeTextColor: Color,
    inactiveTextColor: Color,
) {
    val shape = RoundedCornerShape(percent = 50)

    val background by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        animationSpec = tween(durationMillis = 180),
        label = "chip-bg",
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) activeTextColor else inactiveTextColor,
        animationSpec = tween(durationMillis = 180),
        label = "chip-text",
    )

    Surface(
        onClick = onClick,
        shape = shape,
        color = background,
        shadowElevation = StatusChipsDefaults.Elevation,
        tonalElevation = 0.dp,
    ) {
        Text(
            text = label,
            color = textColor,
            style = AppTypography.bodyMdMedium,
            modifier = Modifier.padding(
                horizontal = StatusChipsDefaults.HorizontalPadding,
                vertical = StatusChipsDefaults.VerticalPadding,
            ),
        )
    }
}