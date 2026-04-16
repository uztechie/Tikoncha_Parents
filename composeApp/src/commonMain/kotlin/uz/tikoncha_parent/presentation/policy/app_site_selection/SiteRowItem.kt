package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.global
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun SiteRowItem(
    modifier: Modifier = Modifier,
    site: SiteUi,
    isSelected: Boolean,
    enabled: Boolean = true,
    onToggle: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .combinedClickable(
                indication = null,
                interactionSource = null,
                onClick = { if (enabled) onToggle() },
                onLongClick = { if (enabled) onLongClick() },
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.global),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = AppColors.icon.secondary,
        )
        Space(16.dp)
        Text(
            text = site.url,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppColors.text.primary,
            style = AppTypography.titleMdMedium,
        )
        Space(10.dp)
        AppCheckbox(
            checked = isSelected,
            onCheckedChange = { if (enabled) onToggle() }
        )
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        SiteRowItem(
            site = SiteUi(url = "instagram.com"),
            isSelected = true,
            enabled = true
        )
    }
}