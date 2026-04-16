package uz.tikoncha_parent.presentation.policy.policy_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.policy.PolicyAction
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*


@Composable
fun PolicyListItem(
    modifier: Modifier = Modifier,
    policy: PolicyItemUi,
    onClick: () -> Unit,
) {
    val statusBgColor = if (policy.isActive) AppColors.bg.primaryContainer
    else AppColors.action.disabledTertiary

    val statusTextColor = if (policy.isActive) AppColors.text.accentEmphasis
    else AppColors.text.disabledTertiary

    val activeText = if (policy.isActive) stringResource(Res.string.faol)
    else stringResource(Res.string.faol_emas)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .simpleShadow(shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.bg.surface)
            .padding(top = 12.dp, bottom = 18.dp),
    ) {
        // ── Sarlavha va faollik ──────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ContainerPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = policy.policyName,
                style = AppTypography.titleLgSemiBold,
                color = AppColors.text.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            SpaceSmall()
            Row(
                modifier = Modifier
                    .background(statusBgColor, CircleShape)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.dot),
                    contentDescription = null,
                    tint = statusTextColor,
                    modifier = Modifier.size(8.dp),
                )
                SpaceUltraSmall()
                Text(
                    maxLines = 1,
                    text = activeText,
                    style = AppTypography.bodyMdMedium,
                    color = statusTextColor,
                )
            }
        }

        Space(12.dp)
        HorizontalDivider(
            color = AppColors.border.secondarySubtle,
            thickness = 1.dp
        )
        Space(12.dp)

        // ── Cheklovlar soni ──────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ContainerPadding),
        ) {
            Text(
                text = stringResource(Res.string.cheklovlar),
                style = AppTypography.bodySmMedium,
                color = AppColors.text.secondary,
                lineHeight = SmallTextSize,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (policy.appCount > 0) {
                    Text(
                        text = stringResource(Res.string.ta_ilovaga, policy.appCount),
                        style = AppTypography.titleSmSemiBold,
                        color = AppColors.text.primary,
                    )
                }
                if (policy.webCount > 0) {
                    Text(
                        text = stringResource(Res.string.ta_vebsaytga, policy.webCount),
                        style = AppTypography.titleSmSemiBold,
                        color = AppColors.text.primary,
                    )
                }
            }

        }

    }
}

@Composable
private fun RuleIconCircle(iconRes: DrawableResource) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(AppColors.action.tertiary),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = AppColors.icon.accentPrimary,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Preview
@Composable
private fun PolicyListItemPreview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        PolicyListItem(
            modifier = Modifier.fillMaxWidth(),
            policy = PolicyItemUi(
                policyName = "Maktab",
                ruleId = "",
                policyType = PolicyType.STUDENT,
                isMine = true,
                appCount = 1,
                webCount = 3,
                hasTimeRule = true,
                hasLimitRule = true,
                hasLocationRule = true,
                limitRule = emptyList(),
                timeRule = emptyList(),
                isActive = false,
                packages = emptyList(),
                categories = emptyList(),
                action = PolicyAction.ALLOW,
                sites = emptyList(),
                locationRule = null
            ),
            onClick = {}
        )
    }
}