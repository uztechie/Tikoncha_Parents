package uz.tikoncha_parent.presentation.policy.time_rule

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
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import qrgenerator.qrkitpainter.text
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bed_sleeping
import tikoncha_parents.composeapp.generated.resources.close_remove
import tikoncha_parents.composeapp.generated.resources.har_kuni
import tikoncha_parents.composeapp.generated.resources.kun_davomida
import tikoncha_parents.composeapp.generated.resources.sleep_large_icon
import tikoncha_parents.composeapp.generated.resources.timer
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.policy.TimeProgressCircle
import uz.tikoncha_parent.presentation.policy.TimeProgressCircleDefaults
import uz.tikoncha_parent.presentation.policy.common.toHhMm
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun TimeRuleItem(
    modifier: Modifier = Modifier,
    item: TimeRuleUi,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    canRemove: Boolean = false
) {
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (item.allDay){
                    TimeRuleIconTextRow(
                        painter = painterResource(Res.drawable.bed_sleeping),
                        text = stringResource(Res.string.kun_davomida)
                    )
                }
                else{
                    TimeRuleIconTextRow(
                        painter = painterResource(Res.drawable.bed_sleeping),
                        text = item.startTime.toHhMm()
                    )
                    Space(4.dp)
                    TimeRuleIconTextRow(
                        painter = painterResource(Res.drawable.timer),
                        text = item.endTime.toHhMm()
                    )
                }
            }

            Space(10.dp)
            if (item.allDay){
                Image(
                    painter = painterResource(Res.drawable.sleep_large_icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                )
            }
            else{
                TimeProgressCircle(
                    minutes = item.perDayMinutes,
                    modifier = Modifier
                        .size(80.dp),
                    strokeWidth = 14.dp,
                    colors = TimeProgressCircleDefaults.colors(

                    )
                )
            }

        }
    }

}

@Composable
fun TimeRuleIconTextRow(
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
        Space(12.dp)
        Text(
            text = text,
            style = AppTypography.headlineMdSemiBold,
            color = AppColors.text.secondary
        )
    }
}

@Preview
@Composable
fun PRe(){
    TikonchaParentTheme {
        TimeRuleItem(
            modifier = Modifier,
            item = TimeRuleUi(
                id = 1,
                startTime = LocalTime(10, 0),
                endTime = LocalTime(12, 0),
                reverse = false,
                allDay = false,
                weekDays = setOf(WeekDay.MON, WeekDay.WED)
            ),
            onRemove = {},
            onClick = {},
            canRemove = true
        )
    }
}