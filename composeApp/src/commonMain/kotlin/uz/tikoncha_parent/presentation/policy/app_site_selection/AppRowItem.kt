package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButtonDefaults.LargeIconSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ic_launcher_foreground
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme


@Composable
fun AppRowItem(
    modifier: Modifier = Modifier,
    app: AppSelectionUi,
    isSelected: Boolean,
    enabled: Boolean = true,
    onToggle: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(AppColors.bg.surface)
            .singleClick {if (enabled) onToggle()},
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        )
        {

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                model = app.iconUrl,
                contentDescription = null,
                placeholder = painterResource(Res.drawable.ic_launcher_foreground),
                error = painterResource(Res.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop
            )
        }

        Space(16.dp)

        Text(
            text = app.name,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppColors.text.primary,
            style = AppTypography.titleMdMedium
        )
        Space(8.dp)

        AppCheckbox(
            checked = isSelected,
            onCheckedChange = { if (enabled) onToggle() }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme() {
        AppRowItem(
            app = AppSelectionUi(
                name = "Instagram",
                packageName = "",
                iconUrl = "",
                order = 0
            ),
            isSelected = true,
            onToggle = {}
        )
    }
}