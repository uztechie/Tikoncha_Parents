package uz.saidburxon.newedu.presentation.feature.assignment

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.datetime.*
import kotlinx.datetime.LocalDate
import uz.tikoncha_parent.common.Util
import uz.tikoncha_parent.common.Util.getMonthName
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CalendarDialog(
    selectedDate: LocalDate?,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {

    fun LocalDate.withDayOfMonth(day: Int): LocalDate {
        return LocalDate(this.year, month.number, day)
    }

    fun LocalDate.lengthOfMonth(): Int {
        val nextMonth = this.plus(DatePeriod(months = 1)).withDayOfMonth(1)
        return nextMonth.minus(DatePeriod(days = 1)).day
    }

    var currentMonth by remember {
        mutableStateOf(selectedDate ?: Util.getCurrentDate().withDayOfMonth(1))
    }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        containerColor = MaterialTheme.extendedColor.backgroundColor,
        modifier = Modifier.fillMaxWidth(),
        text = {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minus(DatePeriod(months = 1)) }) {

                        Icon(
                            painter = painterResource(Res.drawable.arrow_down),
                            contentDescription = "Back"
                        )
                    }

                    CustomText(
                        text = getMonthName(currentMonth) + " ${currentMonth.year}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { currentMonth = currentMonth.plus(DatePeriod(months = 1)) }) {

                       Icon(
                            painter = painterResource(Res.drawable.arrow_next),
                            null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val daysOfWeek = listOf("D", "S", "Ch", "P", "J", "Sh", "Y")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.extendedColor.borderColor)
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    daysOfWeek.forEach {
                        CustomText(
                            text = it,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val firstDayOfMonth = currentMonth
                val lastDay = currentMonth.lengthOfMonth()
                val startDayOfWeek = firstDayOfMonth.withDayOfMonth(1).dayOfWeek.isoDayNumber % 7
                val totalCells = lastDay + startDayOfWeek
                val rows = (totalCells + 6) / 7

                Column {
                    var dayCounter = 1
                    for (i in 0 until rows) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (j in 0..6) {
                                val index = i * 7 + j
                                if (index < startDayOfWeek || dayCounter > lastDay) {
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp)
                                    )
                                } else {
                                    val date = currentMonth.withDayOfMonth(dayCounter)
                                    val isSelected = date == tempSelectedDate

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp)
                                            .clickable { tempSelectedDate = date },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .background(PrimaryColor, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CustomText(
                                                    text = dayCounter.toString(),
                                                    color = MaterialTheme.extendedColor.backgroundColor
                                                )
                                            }
                                        } else {
                                            CustomText(
                                                text = dayCounter.toString(),
                                            )
                                        }
                                    }
                                    dayCounter++
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomOutlinedButton(
                    text = stringResource(Res.string.bekor_qilish),
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .height(DialogButtonHeight)
                        .fillMaxWidth(),
                    textColor = PrimaryColor,
                    borderColor = BorderColor,
                    shape = RoundedCornerShape(TextFieldCornerRadius)
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    onClick = {
                        tempSelectedDate?.let { onDateSelected(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius))
                        .height(DialogButtonHeight),
                    enabled = tempSelectedDate != null,
                    text = stringResource(Res.string.saqlash),
                    shape = RoundedCornerShape(TextFieldCornerRadius)
                )
            }
        }
    )
}

fun reformattedYearDay(reformatedDate: LocalDate?): String {
    if (reformatedDate == null) return ""

    val day = reformatedDate.day.toString().padStart(2, '0')
    val month = reformatedDate.month.number.toString().padStart(2, '0')
    val year = reformatedDate.year.toString()

    return "$day.$month.$year"
}

