package uz.tikoncha_parent.presentation.home.schedule.time

import ScheduleTimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.boshlanishi
import tikoncha_parents.composeapp.generated.resources.tugashi
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.ui.SmallTextSize

enum class Editing { START, END }

@Composable
fun RangesList(
    ranges: List<MinuteRange>,
    onDelete: (index: Int) -> Unit,
    onEdit: (index: Int, newStart: Int, newEnd: Int) -> Unit,
) {

    var editing by remember { mutableStateOf(Editing.START) }
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }
    var pickerTime by remember { mutableStateOf<LocalTime?>(null) }

    var showPicker by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ranges.forEachIndexed { index, range ->
//            val label = if (editingIndex == index && showPicker) {
//                // Dialog ochiq paytda jonli label
//                val startMin = if (editing == Editing.START)
//                    (pickerTime ?: startTime ?: range.start.toLocalTime()).toMinutesOfDay()
//                else
//                    (startTime ?: range.start.toLocalTime()).toMinutesOfDay()
//
//                val endMin = if (editing == Editing.END)
//                    (pickerTime ?: endTime ?: range.end.toLocalTime()).toMinutesOfDay()
//                else
//                    (endTime ?: range.end.toLocalTime()).toMinutesOfDay()
//
//                "${startMin.asHm()} - ${endMin.asHm()}"
//            } else {
//                // Har doim mavjud range qiymatlari
//                "${range.start.asHm()} - ${range.end.asHm()}"
//            }

            val label = "${range.start.asHm()} - ${range.end.asHm24()}"

            RangeRow(
                label = label,
                onClick = {
                    editingIndex = index
                    startTime = range.start.toLocalTime()
                    endTime = range.end.toLocalTime()
                    editing = Editing.START
                    showPicker = true
                },
                onDelete = { onDelete(index) }
            )
        }
        if (ranges.isEmpty()) {
            CustomText(text = "Время не выбрано")
        }
    }

    // Dialogni faqat item bosilganda ko‘rsatamiz
    val index = editingIndex
    if (index != null) {
        val range = ranges.getOrNull(index)
        if (range == null) {
            editingIndex = null
        } else {
//            key(editing){

            val sTime = (startTime ?: range.start.toLocalTimeSafe())
            val eTime = (endTime ?: range.end.toLocalTimeSafe())

                val startLabel = "${stringResource(Res.string.boshlanishi)} "
                val endLabel   = "${stringResource(Res.string.tugashi)} "

                ScheduleTimePickerDialog(
                    textToggle = {
                        SegmentedToggle(
                            options = listOf(
                                startLabel to null,
                                endLabel to null
                            ),
                            selectedIndex = if (editing == Editing.START) 0 else 1,
                            onOptionSelected = { i ->
                                editing = if (i == 0) Editing.START else Editing.END
                                pickerTime = if (editing == Editing.START) (startTime ?: sTime) else (endTime ?: eTime)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Normal,
                            fontSize = SmallTextSize,
                        )
                    },
                    show = showPicker,
                    initialTime = pickerTime ?: sTime,
                    onDismiss = {
                        showPicker = false
                        editingIndex = null
                        pickerTime = null
                        editing = Editing.START
                    },
                    onTimeSelected = { picked ->

                        if (editing == Editing.START) {
                            // 1-bosqich: START ni saqlaymiz, dialog yopilmaydi
                            startTime = picked
                            editing = Editing.END
                            pickerTime = endTime ?: range.end.toLocalTime()
                        } else {
                            // 2-bosqich: END ni saqlab, range ni commit qilamiz
                            endTime = picked
                            val newStart = (startTime ?: range.start.toLocalTime()).toMinutesOfDay()
                            val newEnd   = picked.toMinutesOfDay()
                            if (newEnd > newStart) onEdit(index, newStart, newEnd)

                            // Tozalash va yopish
                            showPicker = false
                            editingIndex = null
                            pickerTime = null
                            editing = Editing.START
                        }
                    },
                    onSave = { picked ->
                        if (editing == Editing.START) {
                            // 1-qadam: startni commit qilamiz
                            startTime = picked
                            pickerTime = endTime ?: range.end.toLocalTime()   // end bosqichi uchun slider holati
                            editing = Editing.END           // dialog yopilmaydi, end’ga o‘tamiz
                        } else {
                            // 2-qadam: endni commit qilamiz
                            endTime = picked
                            val newStart = (startTime ?: range.start.toLocalTime()).toMinutesOfDay()
                            val newEnd   =picked.toMinutesOfDay()
                            if (newEnd > newStart) onEdit(index, newStart, newEnd)

                            // tozalash va yopish
                            showPicker = false
                            editingIndex = null
                            pickerTime = null
                        }
                    }
                )
            }
//        }
    }
}

fun LocalTime.toMinutesOfDay(): Int = hour * 60 + minute
fun Int.toLocalTime(): LocalTime = LocalTime(hour = this / 60, minute = this % 60)

private fun Int.two(): String = this.toString().padStart(2, '0')
fun Int.asHm(): String = "${(this/60).two()}:${(this%60).two()}"

fun Int.asHm24(): String {
    return if (this >= 24*60 - 1) "24:00" else this.asHm() // 1439 -> "24:00"
}


fun LocalTime.asHm(): String = "${hour.two()}:${minute.two()}"

private fun Int.toLocalTimeSafe(): LocalTime {
    val m = this.coerceIn(0, 24*60 - 1) // 1439 gacha
    return LocalTime(hour = m / 60, minute = m % 60)
}


