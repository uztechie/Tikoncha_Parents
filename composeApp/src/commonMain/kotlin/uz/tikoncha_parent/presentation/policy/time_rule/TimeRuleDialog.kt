package uz.tikoncha_parent.presentation.policy.time_rule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.boshlanishi
import tikoncha_parents.composeapp.generated.resources.oraliq_qoshish
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.tugashi
import tikoncha_parents.composeapp.generated.resources.ushbu_oraliqdan_tashqari
import tikoncha_parents.composeapp.generated.resources.vaqt
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.base.coverShadow
import uz.tikoncha_parent.presentation.policy.RoundedCheckbox
import uz.tikoncha_parent.presentation.policy.ScheduleTimeSelection
import uz.tikoncha_parent.presentation.policy.WeekdayChips
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun TimeRuleDialog(
    show: Boolean,
    state: TimeRuleState,
    event: (TimeRuleEvent) -> Unit,
    onDismiss: () -> Unit
)
{


    val startLabel = stringResource(Res.string.boshlanishi) to null
    val endLabel = stringResource(Res.string.tugashi) to null

    var startSelectedHour by remember { mutableStateOf(state.startTime.hour) }

    var startSelectedMinute by remember { mutableStateOf(state.startTime.minute) }


    var endSelectedHour by remember { mutableStateOf(state.endTime.hour) }
    var endSelectedMinute by remember { mutableStateOf(state.endTime.minute) }


    var timeTypeIndex by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(show) {
        if (show) {
            startSelectedHour = state.startTime.hour
            startSelectedMinute = state.startTime.minute
            endSelectedHour = state.endTime.hour
            endSelectedMinute = state.endTime.minute
            timeTypeIndex = 0
        }
    }

    LaunchedEffect(show, startSelectedHour, startSelectedMinute, endSelectedHour, endSelectedMinute){
        if (show){
            event(TimeRuleEvent.SetTimeRule(
                startTime = LocalTime(startSelectedHour, startSelectedMinute),
                endTime = LocalTime(endSelectedHour, endSelectedMinute)
            ))
        }

    }






    if (!show){
        return
    }




    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    )
    {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .coverShadow(
                    radius = CardCornerRadius
                ),
            shape = RoundedCornerShape(CardCornerRadius),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.extendedColor.backgroundColor)
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ContainerPadding)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = stringResource(Res.string.oraliq_qoshish),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor,
                        modifier = Modifier
                            .weight(1f)
                    )

                    CloseButton {
                        onDismiss()
                    }

                }


                SpaceMedium()

                WeekdayChips(
                    chips = state.weekDays,
                    onToggle = {
                        event(TimeRuleEvent.SelectDay(it))
                    }
                )


                SpaceMedium()
                HorizontalDivider()
                SpaceMedium()

                CustomText(
                    text = stringResource(Res.string.vaqt),
                    color = MaterialTheme.extendedColor.hintColor
                )


                Timeline(
                    ranges = state.timeRanges,
                    allDay = state.allDay
                )
                SpaceMedium()

                AllDaySwitch(
                    checked = state.allDay,
                    onCheckedChange = { event(TimeRuleEvent.SetAllDay(it)) }
                )

                SpaceMedium()

                if (!state.allDay){
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    )
                    {


                        SegmentedToggle(
                            options = listOf(
                                startLabel,
                                endLabel
                            ),
                            selectedIndex = timeTypeIndex,
                            onOptionSelected = { i ->
                                timeTypeIndex = i
                            },
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Normal,
                            fontSize = SmallTextSize,
                        )


                        SpaceMedium()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {

                            if (timeTypeIndex == 0) {
                                ScheduleTimeSelection(
                                    range = 0..23,
                                    selected = startSelectedHour,
                                    onSelected = {
                                        startSelectedHour = it
                                    }
                                )
                                Spacer(Modifier.size(5.dp))
                                ScheduleTimeSelection(
                                    range = 0..59,
                                    selected = startSelectedMinute,
                                    onSelected = { startSelectedMinute = it }
                                )
                            } else {
                                ScheduleTimeSelection(
                                    range = 0..23,
                                    selected = endSelectedHour,
                                    onSelected = { endSelectedHour = it }
                                )
                                Spacer(Modifier.size(5.dp))
                                ScheduleTimeSelection(
                                    range = 0..59,
                                    selected = endSelectedMinute,
                                    onSelected = { endSelectedMinute = it }
                                )
                            }


                        }
                        SpaceMedium()

                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            RoundedCheckbox(
                                checked = state.selectOutside,
                                onCheckedChange = {
                                    event(TimeRuleEvent.SetOutsideInterval(it))
                                },

                                )
                            SpaceSmall()
                            CustomText(
                                text = stringResource(Res.string.ushbu_oraliqdan_tashqari)
                            )
                        }

                        SpaceMedium()

                    }
                }


                SpaceMedium()


//                CustomOutlinedButton(
//                    text = stringResource(Res.string.bekor_qilish),
//                    onClick = {
//                        event(ScheduleTimeEvent.ClearTime)
//                        onDismiss()
//                    },
//                    modifier = Modifier
//                        .height(DialogButtonHeight)
//                        .fillMaxWidth(),
//                    textColor = PrimaryColor,
//                    borderColor = BorderColor,
//                    shape = RoundedCornerShape(ButtonCornerRadius)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    onClick = {
                        event(TimeRuleEvent.SaveTime)
                        onDismiss()
                    },
                    enabled = state.weekDays.any { it.enabled },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DialogButtonHeight),
                    text = stringResource(Res.string.saqlash),
                    shape = RoundedCornerShape(ButtonCornerRadius),
                    textColor = OnPrimaryColor,
                    fontWeight = FontWeight.SemiBold
                )


            }
        }

    }
}