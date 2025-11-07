package uz.tikoncha_parent.presentation.policy.time_rule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.har_kuni
import tikoncha_parents.composeapp.generated.resources.kun_davomida
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun TimeRuleItem(
    modifier: Modifier = Modifier,
    item: TimeRuleUi,
    onRemove: () -> Unit,
)
{

//    val bgColor = MaterialTheme.extendedColor.backgroundColor
    val bgColor = MaterialTheme.extendedColor.cardColor


    Row(
        modifier = modifier
            .fillMaxWidth()
            .verticalShadow(
                shape = RoundedCornerShape(TextFieldCornerRadius),
                offset = 0.dp
            )
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
                item.weekDays.map { it.weekdayLabel() }.joinToString(", ")
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