package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalPlay
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_right
import tikoncha_parents.composeapp.generated.resources.warning_1
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun PermissionWarningCard(
    title: String,
    body: String,
    videoUrl: String?,
    onVideoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .simpleShadow(RoundedCornerShape(16.dp))
            .background(AppColors.bg.surface)
            .border(
                width = 0.5.dp,
                color = AppColors.border.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppColors.bg.accentWarningContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.warning_1),
                    contentDescription = null,
                    tint = AppColors.icon.accentPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = AppTypography.emphasizedMdMedium,
                    color = AppColors.text.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = body,
                    style = AppTypography.bodyMdRegular,
                    color = AppColors.text.secondary
                )
            }
        }

        if (!videoUrl.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(
                thickness = 0.5.dp,
                color = AppColors.border.secondary
            )
            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onVideoClick(videoUrl) }
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppColors.bg.accentDanger.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = AppColors.icon.accentDanger,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Video qoʻllanmani koʻrish",
                            style = AppTypography.emphasizedSmMedium,
                            color = AppColors.text.primary
                        )
                        Text(
                            text = "YouTube'da ochiladi",
                            style = AppTypography.bodyMdRegular,
                            color = AppColors.text.tertiary
                        )
                    }
                }

                Icon(
                    painter = painterResource(Res.drawable.arrow_right),
                    contentDescription = null,
                    tint = AppColors.icon.accentPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFF3EADE)
@Preview(
    name = "Dark",
    showBackground = true,
    backgroundColor = 0xFF010D01
)
@Composable
private fun PermissionWarningCardPreview() {
    TikonchaParentTheme {
        Box(
            modifier = Modifier
                .background(AppColors.bg.page)
                .padding(16.dp)
        ) {
            PermissionWarningCard(
                title = "Gʻujanak rejimini yoqish kerak",
                body = "Farzandingiz uchun qoʻygan cheklovlaringiz ishlashi uchun uning telefonidagi Gʻujanak rejimini yoqish kerak.",
                videoUrl = "https://youtu.be/5BxitAavLyo?si=FN24FVuGqQP3HJPv",
                onVideoClick = {}
            )
        }
    }
}