package uz.tikoncha_parent.presentation.new_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.farzand_himoyasi
import tikoncha_parents.composeapp.generated.resources.guard
import tikoncha_parents.composeapp.generated.resources.ta_ruxsat_ochirilgan
import tikoncha_parents.composeapp.generated.resources.ta_yangi_sorov_kutilmoqda
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.CardCornerPadding
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun ChildProtectionCard(
    permissionOffCount: Int,
    pendingRequestCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val total = permissionOffCount + pendingRequestCount
    

    val isDanger = permissionOffCount > 0
    val subtitle = if (isDanger) {
        stringResource(Res.string.ta_ruxsat_ochirilgan, permissionOffCount)
    } else {
        stringResource(Res.string.ta_yangi_sorov_kutilmoqda, pendingRequestCount)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .simpleShadow(RoundedCornerShape(CardCornerRadius))
            .background(AppColors.bg.surface, RoundedCornerShape(CardCornerRadius))
            .clip(RoundedCornerShape(CardCornerRadius))
            .singleClick { onClick() }
            .padding(CardCornerPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.farzand_himoyasi),
                style = AppTypography.displaySmRegular,
                color = AppColors.text.primary,
            )
            if (total != 0) {
                Text(
                    text = subtitle,
                    style = AppTypography.titleSmMedium,
                    color = if (isDanger) AppColors.text.accentDanger else AppColors.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier.padding(end = 5.6.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.guard),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
            )

            if (total != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                        .size(20.dp)
                        .background(
                            if (isDanger) AppColors.bg.accentDanger else AppColors.bg.accentWarning,
                            CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (total > 99) "99" else total.toString(),
                        style = AppTypography.bodySmSemiBold,
                        color = AppColors.text.inverse,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChildProtectionCardPreview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        Column(
            modifier = Modifier
                .background(AppColors.bg.page)
                .padding(16.dp)
        ) {
            ChildProtectionCard(
                permissionOffCount = 1,
                pendingRequestCount = 2,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
            Space(12.dp)
            ChildProtectionCard(
                permissionOffCount = 0,
                pendingRequestCount = 0,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}