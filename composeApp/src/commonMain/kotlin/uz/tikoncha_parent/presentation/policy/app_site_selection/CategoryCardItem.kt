package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_down_reg
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

/**
 * Kategoriyalar tab uchun expandable card.
 * - Header: emoji + name + app count + checkbox (faqat ON/OFF)
 * - Body (expand bo'lsa): kategoriya ichidagi applar (CategoryAppRow — no checkbox)
 */
@Composable
fun CategoryCardItem(
    modifier: Modifier = Modifier,
    group: CategoryGroupUi,
    isSelected: Boolean,
    expanded: Boolean,
    enabled: Boolean,
    onToggleExpand: () -> Unit,
    onToggleSelect: () -> Unit,
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(300),
        label = "arrow_rotation",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        // ── Header ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = onToggleExpand,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow_down_reg),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotationAngle),
                tint = AppColors.icon.secondary,
            )

            Space(16.dp)

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(AppColors.bg.surface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = group.emoji,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                )
            }

            Space(16.dp)

            Text(
                text = group.displayName,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = AppColors.text.primary,
                style = AppTypography.titleMdMedium,
            )

            Space(8.dp)

            Text(
                text = group.apps.size.toString(),
                color = AppColors.text.secondary,
                style = AppTypography.titleSmMedium,
            )

            Space(8.dp)

            AppCheckbox(
                checked = isSelected,
                enabled = enabled,
                onCheckedChange = { if (enabled) onToggleSelect() },
                modifier = Modifier.size(24.dp),
            )
        }

        // ── Body (expand bo'lsa applar ko'rinadi) ──
        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.bg.surface)
                    .padding(start = 48.dp, end = 16.dp),
            ) {
                group.apps.forEach { app ->
                    CategoryAppRow(
                        modifier = Modifier.fillMaxWidth(),
                        app = app,
                    )
                }
            }
        }
    }
}