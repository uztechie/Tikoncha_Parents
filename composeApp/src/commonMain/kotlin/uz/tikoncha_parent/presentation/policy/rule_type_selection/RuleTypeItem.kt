package uz.tikoncha_parent.presentation.policy.rule_type_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.location
import uz.tikoncha_parent.App
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.SoonBox
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleType
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun RuleTypeItem(
    modifier: Modifier = Modifier,
    ruleTypeUi: RuleTypeUi,
    onClick: () -> Unit
) {


    val enabled = ruleTypeUi.enabled

    val bgColor =
        if (enabled) AppColors.section.tertiary else AppColors.section.tertiary.copy(alpha = 0.5f)
    val contentColor =
        if (enabled) AppColors.text.primary else AppColors.text.primary.copy(0.5f)


    var updatedModifier = modifier

    if (enabled) {
        updatedModifier = modifier
            .fillMaxWidth()
    } else {
        updatedModifier = modifier
            .fillMaxWidth()
    }


    Box {
        Row(
            modifier = updatedModifier
                .background(
                    bgColor, RoundedCornerShape(24.dp)
                )
                .padding(12.dp)
                .clickable(
                    enabled = ruleTypeUi.enabled,
                    interactionSource = null,
                    indication = null,
                    onClick = onClick
                ),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (enabled) AppColors.button.accentEmphasisPressed else AppColors.section.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = ruleTypeUi.icon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(16.dp),
                    tint = if (enabled) AppColors.icon.accentPrimary else AppColors.icon.secondary
                )
            }

            Space(12.dp)

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ruleTypeUi.title,
                    color = contentColor,
                    style = AppTypography.titleMdSemiBold
                )

                Space(4.dp)
                Text(
                    text = ruleTypeUi.subtitle,
                    color = contentColor,
                    style = AppTypography.bodyMdRegular

                )
            }
        }

//        if (ruleTypeUi.soon) {
//            SoonBox(
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//            )
//
//        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        RuleTypeItem(
            ruleTypeUi = RuleTypeUi(
                type = RuleType.LOCATION,
                icon = painterResource(Res.drawable.location),
                title = "Lokatsiya",
                subtitle = "Salom elon \n sasak",
                enabled = false,
                hasItems = true,
                soon = true,
            ),
            onClick = {}
        )
    }
}