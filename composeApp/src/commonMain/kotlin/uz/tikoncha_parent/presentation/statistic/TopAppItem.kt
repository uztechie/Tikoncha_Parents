package uz.tikoncha_parent.presentation.statistic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.android
import tikoncha_parents.composeapp.generated.resources.media_play
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.ui.AppIconSize
import uz.tikoncha_parent.ui.ContainerCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun TopAppItem(
    app: TopAppUi,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppIconSize)
                .clip(RoundedCornerShape(ContainerCornerRadius))
                .background(AppColors.bg.tertiary),
            contentAlignment = Alignment.Center,
        ) {
            if (!app.iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = app.iconUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    error = painterResource(Res.drawable.android),
                    placeholder = painterResource(Res.drawable.android)
                )
            }
            else{
                Icon(
                    painter = painterResource(Res.drawable.android),
                    contentDescription = "",
                    tint = AppColors.icon.accentPrimary,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = app.name,
            style = AppTypography.titleSmMedium,
            color = AppColors.text.primary,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = durationStringShort(app.usage),
            style = AppTypography.titleSmMedium,
            color = AppColors.text.secondary,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun TopAppItemPreview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        Column(
            modifier = Modifier
                .background(AppColors.bg.surface)
                .padding(ContainerPadding)
        ) {
            TopAppItem(
                app = TopAppUi(
                    packageName = "com.android.chrome",
                    name = "Chrome",
                    iconUrl = null,
                    usageMillis = 9_800_000L,
                    usage = HourMinute(2, 43),
                )
            )
            DividerHorizontal()
            TopAppItem(
                app = TopAppUi(
                    packageName = "com.instagram.android",
                    name = "Instagram",
                    iconUrl = null,
                    usageMillis = 6_300_000L,
                    usage = HourMinute(1, 45),
                )
            )
            DividerHorizontal()
            TopAppItem(
                app = TopAppUi(
                    packageName = "com.example.verylongapp",
                    name = "Juda uzun nomli ilova bu nima narsa o'zi",
                    iconUrl = null,
                    usageMillis = 600_000L,
                    usage = HourMinute(0, 10),
                )
            )
        }
    }
}

@Preview
@Composable
private fun TopAppItemPreview_Dark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        Column(
            modifier = Modifier
                .background(AppColors.bg.surface)
                .padding(ContainerPadding)
        ) {
            TopAppItem(
                app = TopAppUi(
                    packageName = "com.android.chrome",
                    name = "Chrome",
                    iconUrl = null,
                    usageMillis = 9_800_000L,
                    usage = HourMinute(2, 43),
                )
            )
        }
    }
}