package uz.tikoncha_parent.presentation.home.schedule.timelist

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
import tikoncha_parents.composeapp.generated.resources.foydalanish_chegarasi
import tikoncha_parents.composeapp.generated.resources.har_kuni
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.soatbay
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.base.coverShadow
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ScheduleUsageLimitDialog(
    show: Boolean,
    state: ScheduleTimeState,
    event: (ScheduleTimeEvent) -> Unit,
    onDismiss: () -> Unit
) {

    val daily = stringResource(Res.string.har_kuni) to null
    val hourly = stringResource(Res.string.soatbay) to null

    var dailySelectedHour by remember { mutableStateOf(state.dayHour.hour) }
    var dailySelectedMinute by remember { mutableStateOf(state.dayMinute.minute) }

    
    var selectedMinute by remember { mutableStateOf(state.hourly.minute) }


    var usageTypeIndex by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(show) {
        if (show) {
            dailySelectedHour = state.dayHour.hour
            dailySelectedMinute = state.dayMinute.minute
            selectedMinute = state.hourly.minute
            usageTypeIndex = 0
        }
    }

    LaunchedEffect(show, dailySelectedHour, dailySelectedMinute, selectedMinute){
        if (show){
            event(ScheduleTimeEvent.SetUsageLimitTime(
                dayHour = LocalTime(dailySelectedHour, dailySelectedMinute),
                hourly = LocalTime(0, selectedMinute)
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
                        text = stringResource(Res.string.foydalanish_chegarasi),
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
                    selected = state.usageLimitDays,
                    onToggle = {
                        event(ScheduleTimeEvent.SelectUsageDay(it))
                    }
                )

                SpaceMedium()
                HorizontalDivider()
                SpaceMedium()

                SpaceMedium()
                if (!state.allDay){
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    )
                    {
                        SegmentedToggle(
                            options = listOf(
                                daily,
                                hourly
                            ),
                            selectedIndex = usageTypeIndex,
                            onOptionSelected = { i ->
                                usageTypeIndex = i
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
                            if (usageTypeIndex == 0) {
                                ScheduleTimeColumn(
                                    range = 0..23,
                                    selected = dailySelectedHour,
                                    onSelected = {
                                        dailySelectedHour = it
                                    }
                                )
                                Spacer(Modifier.size(5.dp))
                                ScheduleTimeColumn(
                                    range = 0..59,
                                    selected = dailySelectedMinute,
                                    onSelected = { dailySelectedMinute = it }
                                )
                            } else {
                                ScheduleTimeColumn(
                                    range = 0..59,
                                    selected = selectedMinute,
                                    onSelected = { selectedMinute = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        SpaceMedium()
                        SpaceMedium()
                    }
                }

                SpaceMedium()
                CustomButton(
                    onClick = {
                        event(ScheduleTimeEvent.SaveUsageTime)
                        onDismiss()
                    },
                    enabled = state.usageLimitDays.isNotEmpty(),
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