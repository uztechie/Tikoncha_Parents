package uz.tikoncha_parent.presentation.policy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*


@Composable
fun PolicyListItem(
    modifier: Modifier = Modifier,
    policy: PolicyItemUi,
    onEdit: () -> Unit,
    onClick: () -> Unit,
) {

    val titleIcon = when (policy.policyType) {
        PolicyType.SCHOOL -> painterResource(Res.drawable.school)
        PolicyType.STUDENT -> painterResource(Res.drawable.profile)
        PolicyType.PARENT_CHILD -> painterResource(Res.drawable.family)

    }

    val isActiveColor = if (policy.isActive) {
        MaterialTheme.extendedColor.primaryColor
    } else {
        OtpErrorColor
    }

    val activeText = if (policy.isActive) {
        stringResource(Res.string.faol)
    } else {
        stringResource(Res.string.faol_emas)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .clip(RoundedCornerShape(CardCornerRadius))
            .background(MaterialTheme.extendedColor.cardColor)
            .padding(ContainerPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = titleIcon,
                contentDescription = "clock",
                tint = MaterialTheme.extendedColor.primaryColor,
                modifier = Modifier.size(NormalIconSize)
            )
            SpaceUltraSmall()
            CustomText(
                text = policy.policyName,
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            SpaceSmall()
            Spacer(Modifier.weight(1f))


            Row(
                modifier = Modifier.background(
                    isActiveColor.copy(0.25f),
                    RoundedCornerShape(ContainerCornerRadius / 2)
                )
                    .padding(horizontal = 8.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.dot),
                    contentDescription = "clock",
                    tint = isActiveColor,
                    modifier = Modifier
                        .size(8.dp)
                )
                SpaceUltraSmall()
                CustomText(
                    maxLines = 1,
                    text = activeText,
                    color = isActiveColor,
                    fontSize = UltraSmallTextSize,
                    fontWeight = FontWeight.W600
                )
            }
        }

        SpaceSmall()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            if (policy.appCount > 0) {
                Icon(
                    painter = painterResource(Res.drawable.apps_icon),
                    contentDescription = "clock",
                    tint = MaterialTheme.extendedColor.primaryColor,
                    modifier = Modifier.size(NormalIconSize)
                )
                SpaceSmall()

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    CustomText(
                        text = stringResource(Res.string.cheklovlar),
                        fontSize = SmallTextSize,
                        color = MaterialTheme.extendedColor.hintColor,
                        lineHeight = SmallTextSize
                    )
                    CustomText(
                        text = stringResource(Res.string.ta_ilovaga, policy.appCount),
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W600,
                    )
                }
            }

            if (policy.webCount > 0) {

                Icon(
                    painter = painterResource(Res.drawable.global),
                    contentDescription = "clock",
                    tint = MaterialTheme.extendedColor.primaryColor,
                    modifier = Modifier.size(NormalIconSize)
                )
                SpaceSmall()

                Column {
                    CustomText(
                        text = stringResource(Res.string.cheklovlar),
                        fontSize = SmallTextSize,
                        color = MaterialTheme.extendedColor.hintColor,
                        lineHeight = SmallTextSize
                    )
                    CustomText(
                        text = stringResource(
                            Res.string.ta_vebsaytga,
                            policy.webCount
                        ),
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W600,
                    )
                }
                Spacer(modifier = Modifier.weight(0.5f))
            }
        }




        DividerHorizontal(
            modifier = Modifier
                .padding(vertical = ContainerPadding / 2)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CustomText(
                    text = stringResource(Res.string.shartlar),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (policy.hasTimeRule) {
                        Icon(
                            painter = painterResource(Res.drawable.shift_clock),
                            contentDescription = "clock",
                            tint = MaterialTheme.extendedColor.primaryColor,
                            modifier = Modifier.size(SmallIconSize)
                        )
                    }
                    if (policy.hasLimitRule) {
                        Icon(
                            painter = painterResource(Res.drawable.time_limit),
                            contentDescription = "clock",
                            tint = MaterialTheme.extendedColor.primaryColor,
                            modifier = Modifier.size(SmallIconSize)
                        )
                    }
                    if (policy.hasLocationRule) {
                        Icon(
                            painter = painterResource(Res.drawable.location),
                            contentDescription = "clock",
                            tint = MaterialTheme.extendedColor.primaryColor,
                            modifier = Modifier.size(SmallIconSize)
                        )
                    }
                }
            }


            Column(
                modifier = Modifier.weight(1f)
            )
            {
                CustomText(
                    text = stringResource(Res.string.ustuvorligi),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                )
                {
                    val filledStars = when (policy.policyType) {
                        PolicyType.STUDENT -> 1
                        PolicyType.PARENT_CHILD -> 2
                        PolicyType.SCHOOL -> 3
                    }

                    for (i in 1..3) {

                        Icon(
                            painter = painterResource(Res.drawable.star_vector),
                            contentDescription = "clock",
                            tint = if (i <= filledStars) {
                                Color(0XFFF0BC39)
                            } else {
                                MaterialTheme.extendedColor.hintColor
                            },
                            modifier = Modifier
                                .size(SmallIconSize)
                                .padding(2.dp)
                        )
                    }
                }
            }

            SpaceSmall()

            Image(
                painter = painterResource(Res.drawable.detail_icon),
                contentDescription = "clock",
                modifier = Modifier.size(NormalIconButtonSize)
            )
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        PolicyListItem(
            modifier = Modifier
                .fillMaxWidth(),
            policy = PolicyItemUi(
                policyName = "Maktab",
                policyId = "",
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
                sites = emptyList()
            ),
            onEdit = {},
            onClick = {}
        )
    }
}