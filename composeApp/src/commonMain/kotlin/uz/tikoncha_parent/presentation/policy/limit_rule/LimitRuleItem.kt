package uz.tikoncha_parent.presentation.policy.limit_rule

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
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun LimitRuleItem(
    modifier: Modifier = Modifier,
    item: LimitRuleUi,
    onRemove: () -> Unit,
    canRemove: Boolean = true
)
{

    val bgColor = MaterialTheme.extendedColor.cardColor


    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                bgColor,
                RoundedCornerShape(TextFieldCornerRadius)
            )
            .padding(10.dp)
    )
    {
        SpaceSmall()

        Column(
            modifier = Modifier
                .weight(1f)
        ) {


            Row {
                CustomText(
                    text = stringResource(item.limitType.resId),
                    fontSize = LargeTextSize,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.extendedColor.hintColor
                )

                SpaceSmall()

                val time = StringBuilder()
                if (item.time.hour > 0) {
                    time.append(item.time.hour)
                    time.append(" ")
                    time.append(stringResource(Res.string.soat))
                    time.append(", ")
                }
                if (item.time.minute > 0){
                    time.append(item.time.minute)
                    time.append(" ")
                    time.append(stringResource(Res.string.daqiqa))
                }

                CustomText(
                    text = time.toString(),
                    fontSize = LargeTextSize,
                )
            }

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
        }

        if (canRemove){
            SpaceSmall()

            CloseButton(
                onClick = {
                    onRemove()
                }
            )
        }
    }
}


@Preview
@Composable
private fun PreviewScheduleTimeItem() {
    TikonchaParentTheme(mode = ThemeMode.LIGHT){
        LimitRuleItem(
            item = LimitRuleUi(),
            onRemove = {}
        )
    }

}