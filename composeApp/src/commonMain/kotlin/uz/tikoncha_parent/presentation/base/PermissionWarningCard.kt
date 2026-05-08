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
import androidx.compose.foundation.shape.CircleShape
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
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_right
import tikoncha_parents.composeapp.generated.resources.play
import tikoncha_parents.composeapp.generated.resources.qanday_faollashtiriladi
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.warning_1
import tikoncha_parents.composeapp.generated.resources.warning_filled
import uz.tikoncha_parent.ui.Space
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
            .simpleShadow(RoundedCornerShape(20.dp))
            .background(AppColors.bg.surface, RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppColors.bg.accentWarningContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.warning_filled),
                    contentDescription = null,
                    tint = AppColors.icon.accentWarning,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    style = AppTypography.titleSmSemiBold,
                    color = AppColors.text.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = body,
                    style = AppTypography.emphasizedXsMedium,
                    color = AppColors.text.secondary
                )
            }
        }

        if (!videoUrl.isNullOrBlank()) {
            Spacer(Modifier.height(16.dp))
            CustomButtonNew(
                text = stringResource(Res.string.qanday_faollashtiriladi),
                onClick = {
                    onVideoClick(videoUrl)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.play),
                        contentDescription = "play",
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            )
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