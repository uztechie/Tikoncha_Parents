package uz.tikoncha_parent.presentation.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.daqiqa
import tikoncha_parents.composeapp.generated.resources.ic_launcher_foreground
import tikoncha_parents.composeapp.generated.resources.question
import tikoncha_parents.composeapp.generated.resources.soat
import tikoncha_parents.composeapp.generated.resources.time_icon
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.presentation.policy.common.formatDuration
import uz.tikoncha_parent.ui.AppIconInnerPadding
import uz.tikoncha_parent.ui.AppIconSize
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun AppUsageItem(
    appUsageUi: AppUsageUi,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(iconUrl = appUsageUi.icon)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = appUsageUi.name,
                style = AppTypography.titleSmSemiBold,
                color = AppColors.text.primary,
                maxLines = 1,
            )

            Spacer(Modifier.height(2.dp))

            UsageDuration(time = appUsageUi.usageTime)
        }
    }
}

// ═════════════════════════════════════════════════════════════
// Icon
// ═════════════════════════════════════════════════════════════

@Composable
private fun AppIcon(
    iconUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.bg.surface),
        contentAlignment = Alignment.Center,
    ) {
        if (iconUrl.isBlank()) {
            FallbackIcon()
        } else {
            AsyncImage(
                model = ImageRequest.Builder(coil3.compose.LocalPlatformContext.current)
                    .data(iconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize(),
                error = painterResource(Res.drawable.ic_launcher_foreground),
                fallback = painterResource(Res.drawable.ic_launcher_foreground),
            )
        }
    }
}

@Composable
private fun FallbackIcon() {
    Icon(
        painter = painterResource(Res.drawable.question),
        contentDescription = null,
        tint = AppColors.icon.accentPrimary,
        modifier = Modifier.fillMaxSize(),
    )
}

// ═════════════════════════════════════════════════════════════
// Usage duration row
// ═════════════════════════════════════════════════════════════

@Composable
private fun UsageDuration(
    time: HourMinute,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.time_icon),
            contentDescription = null,
            tint = AppColors.icon.accentPrimary,
            modifier = Modifier.size(SmallIconSize),
        )

        Text(
            text = formatDuration(time.toMinutes()),
            style = AppTypography.emphasizedSmMedium,
            color = AppColors.text.secondary,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

// ═════════════════════════════════════════════════════════════
// Preview
// ═════════════════════════════════════════════════════════════

@Preview
@Composable
private fun AppUsageItemPreviewLight() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        AppUsageItem(
            appUsageUi = AppUsageUi(
                packageName = "com.instagram.android",
                name = "Instagram",
                icon = "",
                usageTime = HourMinute(2, 35),
            ),
        )
    }
}

@Preview
@Composable
private fun AppUsageItemPreviewDark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        AppUsageItem(
            appUsageUi = AppUsageUi(
                packageName = "com.instagram.android",
                name = "Instagram",
                icon = "",
                usageTime = HourMinute(2, 35),
            ),
        )
    }
}