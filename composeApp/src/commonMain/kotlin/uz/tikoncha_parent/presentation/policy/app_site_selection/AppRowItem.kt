package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_down_reg
import tikoncha_parents.composeapp.generated.resources.ic_launcher_foreground
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

@Composable
fun AppRowItem(
    modifier: Modifier = Modifier,
    app: AppSelectionUi,
    isSelected: Boolean,
    coveredByCategory: Boolean = false,
    enabled: Boolean = true,
    expandable: Boolean = false,
    expanded: Boolean = false,
    onExpandToggle: () -> Unit = {},
    onToggle: () -> Unit,
) {
    val effectiveChecked = isSelected
    val checkboxEnabled = enabled && !coveredByCategory

    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(300),
        label = "app_arrow_rotation",
    )

    // Arrow faqat expandable VA covered emas bo'lsa ko'rinadi
    val showArrow = expandable && !coveredByCategory

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .singleClick {
                if (!enabled) return@singleClick
                when {
                    coveredByCategory -> onToggle()    // toast (screen orqali)
                    expandable -> onExpandToggle()
                    else -> onToggle()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // App icon
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                model = app.iconUrl,
                contentDescription = null,
                placeholder = painterResource(Res.drawable.ic_launcher_foreground),
                error = painterResource(Res.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop,
            )
        }

        Space(16.dp)

        // Title
        Text(
            text = app.name,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppColors.text.primary,
            style = AppTypography.titleMdMedium,
        )

        Space(8.dp)

        // Arrow (faqat expandable + not covered)
        if (showArrow) {
            Icon(
                painter = painterResource(Res.drawable.arrow_down_reg),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotationAngle),
                tint = AppColors.icon.secondary,
            )
            Space(8.dp)
        }

        // Checkbox
        AppCheckbox(
            checked = effectiveChecked,
            enabled = checkboxEnabled,
            onCheckedChange = { if (enabled) onToggle() },
        )
    }
}