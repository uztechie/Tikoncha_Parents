package uz.tikoncha_parent.presentation.policy.limit_rule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.presentation.policy.TimeProgressCircle
import uz.tikoncha_parent.presentation.policy.TimeProgressCircleDefaults
import uz.tikoncha_parent.presentation.policy.common.formatDuration
import uz.tikoncha_parent.presentation.policy.common.toHhMm
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleIconTextRow
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun LimitRuleItem(
    modifier: Modifier = Modifier,
    item: LimitRuleUi,
    onRemove: () -> Unit,
    onClick: () -> Unit,
    canRemove: Boolean = true
)
{
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.bg.surface, shape = RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween.also { Arrangement.spacedBy(10.dp) }
        ) {
            Space(24.dp)
            Row(
                horizontalArrangement = Arrangement
                    .spacedBy(space = 12.dp, alignment = Alignment.CenterHorizontally,),
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                item.weekDays.forEach {
                    Text(
                        text = it.weekdayLabel(),
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.secondary
                    )
                }
            }


            if (canRemove){
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(24.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = AppColors.modal.secondary,
                        contentColor = AppColors.icon.secondary
                    )
                ){
                    Icon(
                        painter = painterResource(Res.drawable.close_remove),
                        contentDescription = "",
                        modifier = Modifier
                            .size(14.dp)
                    )
                }
            }
            else{
                Space(24.dp)
            }


        }
        HorizontalDivider(
            thickness = 1.dp,
            color = AppColors.border.secondarySubtle
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            LimitRuleIconTextRow(
                painter = painterResource(Res.drawable.bed_sleeping),
                text = stringResource(item.limitType.resId),
                modifier = Modifier
                    .weight(1f)
            )

            Space(10.dp)
            Text(
                text = formatDuration(item.time.toMinutes()),
                style = AppTypography.titleMdSemiBold,
                color = AppColors.text.accentEmphasis
            )

        }
    }
}


@Composable
fun LimitRuleIconTextRow(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
            tint = AppColors.icon.secondary
        )
        Space(4.dp)
        Text(
            text = text,
            style = AppTypography.titleSmSemiBold,
            color = AppColors.text.primary
        )
    }
}

@Preview
@Composable
private fun PreviewScheduleTimeItem() {
    TikonchaParentTheme(mode = ThemeMode.LIGHT){
        LimitRuleItem(
            item = LimitRuleUi(),
            onRemove = {},
            onClick = {}
        )
    }

}