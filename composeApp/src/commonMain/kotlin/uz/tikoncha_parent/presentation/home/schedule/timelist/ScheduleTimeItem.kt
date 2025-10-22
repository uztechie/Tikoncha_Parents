package uz.tikoncha_parent.presentation.home.schedule.timelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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


import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_left
import tikoncha_parents.composeapp.generated.resources.ch
import tikoncha_parents.composeapp.generated.resources.clock
import tikoncha_parents.composeapp.generated.resources.close
import tikoncha_parents.composeapp.generated.resources.close_circle
import tikoncha_parents.composeapp.generated.resources.du
import tikoncha_parents.composeapp.generated.resources.har_kuni
import tikoncha_parents.composeapp.generated.resources.ju
import tikoncha_parents.composeapp.generated.resources.kun_davomida
import tikoncha_parents.composeapp.generated.resources.kunlik
import tikoncha_parents.composeapp.generated.resources.pa
import tikoncha_parents.composeapp.generated.resources.se
import tikoncha_parents.composeapp.generated.resources.sh
import tikoncha_parents.composeapp.generated.resources.ya
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.MinuteRange
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.domain.model.hm
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.ui.BorderColor
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalIconButtonPadding
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun ScheduleTimeItem(
    modifier: Modifier = Modifier,
    item: ScheduleTimeUi,
    onRemove: () -> Unit,
)
{

//    val bgColor = MaterialTheme.extendedColor.backgroundColor
    val bgColor = Color(0xFFFFFFFF)


    Row(
        modifier = modifier
            .fillMaxWidth()
            .verticalShadow()
            .background(
                bgColor,
                RoundedCornerShape(TextFieldCornerRadius)
            )
            .padding(10.dp)
    )
    {

//        Box(
//            modifier = Modifier
//                .size(SmallIconButtonSize)
//                .clip(RoundedCornerShape(ShapeCornerRadius))
//                .background(MaterialTheme.extendedColor.tonalButtonColor),
//            contentAlignment = Alignment.Center
//        ) {
//            Image(
//                painter = painterResource(Res.drawable.clock),
//                contentDescription = "",
//                modifier = Modifier
//                    .fillMaxSize(0.6f)
//            )
//        }

        SpaceSmall()

        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            val title = if (item.allDay){
                stringResource(Res.string.kun_davomida)
            }
            else{
                item.time
            }

            CustomText(
                text = title,
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )
            val labels = if (item.weekDays.size == 7){
                stringResource(Res.string.har_kuni)
            }
            else{
                item.weekDays.map { weekdayLabel(it) }.joinToString(", ")
            }

            CustomText(
                text = labels,
                fontSize = SmallTextSize,
                fontWeight = FontWeight.Normal,
                lineHeight = NormalTextSize,
                color = MaterialTheme.extendedColor.hintColor
            )

            SpaceSmall()

            Timeline(
                ranges = item.timeRange,
                allDay = item.allDay
            )
        }


        SpaceSmall()

        CloseButton(
            onClick = {
                onRemove()
            }
        )
    }
}