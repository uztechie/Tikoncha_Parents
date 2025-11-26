package uz.tikoncha_parent.presentation.policy.app_selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButtonDefaults.LargeIconSize
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
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.policy.RoundedCheckbox
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun AppRowItem(
    modifier: Modifier = Modifier,
    app: AppSelectionUi,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = {
                    if (enabled) onCheckedChange(!app.checked)
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(LargeIconSize),
            contentAlignment = Alignment.Center
        ) {
            if (!app.iconUrl.isNullOrEmpty()) {
                AsyncImage(
                    modifier = Modifier.size(LargeIconSize)
                        .clip(RoundedCornerShape(6.dp)),
                    model = app.iconUrl,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.ic_launcher_foreground),
                    error = painterResource(Res.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop
                )
            } else {

                Image(
                    painter = painterResource(Res.drawable.ic_launcher_foreground),
                    modifier = Modifier.size(LargeIconSize)
                        .clip(RoundedCornerShape(6.dp)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )


            }
        }

        SpaceMedium()

        CustomText(
            text = app.name,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        RoundedCheckbox(
            checked = app.checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        AppRowItem(
            app = AppSelectionUi(
                name = "Instagram",
                packageName = "",
                iconUrl = "",
                checked = true,
                order = 0
            ),
            onCheckedChange = {},
            enabled = false
        )
    }
}