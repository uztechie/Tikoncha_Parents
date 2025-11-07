package uz.tikoncha_parent.presentation.policy.rule_type_selection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.SoonBox
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleListScreen
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleListScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun RuleTypeItem(
    modifier: Modifier = Modifier,
    ruleTypeUi: RuleTypeUi,
    onClick: () -> Unit
) {


    val enabled = ruleTypeUi.enabled

    val bgColor =
        if (enabled) MaterialTheme.extendedColor.cardColor else MaterialTheme.extendedColor.disabledBgColor
    val contentColor =
        if (enabled) MaterialTheme.extendedColor.textColor else MaterialTheme.extendedColor.disabledContentColor
    val secondaryContentColor =
        if (enabled) MaterialTheme.extendedColor.hintColor else MaterialTheme.extendedColor.disabledContentColor


    var updatedModifier = modifier

    if (enabled) {
        updatedModifier = modifier
            .fillMaxWidth()
            .verticalShadow(
                shape = RoundedCornerShape(TextFieldCornerRadius),
                offset = 0.dp
            )
    } else {
        updatedModifier = modifier
            .fillMaxWidth()
    }


    Box{
        Row(
            modifier = updatedModifier
                .background(
                    bgColor, RoundedCornerShape(TextFieldCornerRadius)
                )
                .padding(10.dp)
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
                    .size(NormalIconButtonSize)
                    .clip(RoundedCornerShape(ShapeCornerRadius)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = ruleTypeUi.icon,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.6f),
                    tint = if (enabled) MaterialTheme.extendedColor.primaryColor else MaterialTheme.extendedColor.disabledContentColor
                )
            }

            SpaceMedium()

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomText(
                    text = ruleTypeUi.title,
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )


                CustomText(
                    text = ruleTypeUi.subtitle,
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.W500,
                    color = secondaryContentColor,

                    )
            }
        }

        if (ruleTypeUi.soon){
            SoonBox(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            )

        }


    }
}