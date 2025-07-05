package uz.saidburxon.newedu.presentation.feature.assignment

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.datetime.*
import org.example.project.platform.getCurrentDate
import org.example.project.platform.getMonthName
import org.example.project.presentation.base.CustomOutlinedButton
import org.example.project.presentation.base.theme.*
import uz.saidburxon.newedu.presentation.base.CustomButton

@Composable
fun CalendarDialog(
    selectedDate: LocalDate?,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {

    fun LocalDate.withDayOfMonth(day: Int): LocalDate {
        return LocalDate(this.year, this.monthNumber, day)
    }

    fun LocalDate.lengthOfMonth(): Int {
        val nextMonth = this.plus(DatePeriod(months = 1)).withDayOfMonth(1)
        return nextMonth.minus(DatePeriod(days = 1)).dayOfMonth
    }

    var currentMonth by remember {
        mutableStateOf(selectedDate ?: getCurrentDate().withDayOfMonth(1))
    }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }
    val today = getCurrentDate()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        containerColor = BackgroundColor,
        modifier = Modifier.fillMaxWidth(),
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minus(DatePeriod(months = 1)) }) {
//                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Oldingi oy")
                    }

                    Text(
                        text = getMonthName(currentMonth) + " ${currentMonth.year}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { currentMonth = currentMonth.plus(DatePeriod(months = 1)) }) {
//                        Icon(
//                            Icons.AutoMirrored.Filled.ArrowForwardIos,
//                            contentDescription = "Keyingi oy"
//                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val daysOfWeek = listOf("D", "S", "Ch", "P", "J", "Sh", "Y")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(WheelPickerSelectionColor)
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    daysOfWeek.forEach {
                        Text(
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
                                                    .background(Color(0xFF4CAF50), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = dayCounter.toString(),
                                                    color = Color.White
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = dayCounter.toString(),
                                                color = Color.Black
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
                    text = "Bekor qilish",
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .height(DialogButtonHeight)
                        .fillMaxWidth(),
                    textColor = PrimaryColor,
                    borderColor = PrimaryColor,
                    shape = RoundedCornerShape(ButtonDialogCornerRadius)
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    onClick = {
                        tempSelectedDate?.let { onDateSelected(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonDialogCornerRadius))
                        .height(DialogButtonHeight),
                    enabled = tempSelectedDate != null,
                    text = "Saqlash",
                    shape = RoundedCornerShape(ButtonDialogCornerRadius)
                )
            }
        }
    )
}
