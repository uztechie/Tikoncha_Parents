package uz.tikoncha_parent.presentation.statistic

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun PagerDots(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    if (total <= 1) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(total) { i ->
            val selected = i == current
            val size by animateDpAsState(
                targetValue = if (selected) 8.dp else 6.dp,
                label = "dotSize"
            )
            val alpha by animateFloatAsState(
                targetValue = if (selected) 1f else 0.35f,
                label = "dotAlpha"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(size)
                    .background(
                        color = AppColors.bg.primary.copy(alpha = alpha),
                        shape = CircleShape,
                    )
            )
        }
    }
}

@Preview
@Composable
private fun PagerDotsPreview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        Column(
            modifier = Modifier
                .background(AppColors.bg.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PagerDots(total = 3, current = 0, modifier = Modifier.fillMaxWidth())
            PagerDots(total = 3, current = 1, modifier = Modifier.fillMaxWidth())
            PagerDots(total = 5, current = 2, modifier = Modifier.fillMaxWidth())
            PagerDots(total = 7, current = 6, modifier = Modifier.fillMaxWidth())
        }
    }
}