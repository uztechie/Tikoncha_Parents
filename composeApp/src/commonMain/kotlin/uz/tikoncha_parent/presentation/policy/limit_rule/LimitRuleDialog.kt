@file:OptIn(ExperimentalResourceApi::class)

package uz.tikoncha_parent.presentation.policy.limit_rule

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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.daq
import tikoncha_parents.composeapp.generated.resources.daqiqa
import tikoncha_parents.composeapp.generated.resources.foydalanish_chegarasi
import tikoncha_parents.composeapp.generated.resources.har_kuni
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.soat
import tikoncha_parents.composeapp.generated.resources.soatbay
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.base.coverShadow
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
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun LimitRuleDialog(
    show: Boolean,
    state: LimitRuleState,
    event: (LimitRuleEvent) -> Unit,
    onDismiss: () -> Unit
) {

    val daily = stringResource(Res.string.har_kuni) to null
    val hourly = stringResource(Res.string.soatbay) to null

    var selectedHour by remember(show) { mutableStateOf(state.hourMinute.hour) }
    var selectedMinute by remember(show) { mutableStateOf(state.hourMinute.minute) }



    LaunchedEffect(show, selectedHour, selectedMinute){
        if (show){
            event(
                LimitRuleEvent.SetTime(
                time = HourMinute(selectedHour, selectedMinute),
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
                    chips = state.weekDays,
                    onToggle = {
                        event(LimitRuleEvent.SelectWeekDay(it))
                    }
                )

                SpaceMedium()
                HorizontalDivider()
                SpaceMedium()

                SpaceMedium()
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
                        selectedIndex = if(state.selectedLimitType == DayHour.DAY) 0 else 1,
                        onOptionSelected = { i ->
                            selectedMinute = 0
                            selectedHour = 0
                            event(
                                LimitRuleEvent.SelectLimitType(
                                    if (i == 0) DayHour.DAY else DayHour.HOUR
                                ))
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
                        if (state.selectedLimitType == DayHour.DAY) {
                            ScheduleTimeSelection(
                                label = stringResource(Res.string.soat),
                                range = 0..23,
                                selected = selectedHour,
                                onSelected = {
                                    selectedHour = it
                                }
                            )
                            Spacer(Modifier.size(5.dp))
                            ScheduleTimeSelection(
                                label = stringResource(Res.string.daq),
                                range = 0..59,
                                selected = selectedMinute,
                                onSelected = { selectedMinute = it }
                            )
                        } else {
                            ScheduleTimeSelection(
                                label = stringResource(Res.string.daqiqa),
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

                SpaceMedium()
                CustomButton(
                    onClick = {
                        event(LimitRuleEvent.SaveLimit)
                        onDismiss()
                    },
                    enabled = (state.weekDays.any { it.selected } && (state.hourMinute.hour != 0 || state.hourMinute.minute != 0)),
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