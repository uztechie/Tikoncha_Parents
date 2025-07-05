package org.example.project.presentation.base

import androidx.compose.foundation.clickable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.ktor.websocket.Frame
import kotlinx.datetime.LocalDate
import org.example.project.presentation.task.reformattedToday

@Composable
fun DateSelectorTextField(
    selectedDate: LocalDate?,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    val dateText = selectedDate?.let { reformattedToday(it) } ?: ""

    OutlinedTextField(
        value = dateText,
        onValueChange = {}, // readonly, hech nima qilmaydi
        modifier = modifier
            .clickable { showDialog = true },
        label = { Frame.Text(text = label) },
        readOnly = true,
        leadingIcon = leadingIcon,
        singleLine = true
    )

//    if (showDialog) {
//        CalendarDialog(
//            selectedDate = selectedDate,
//            onDismissRequest = { showDialog = false },
//            onDateSelected = {
//                onDateChange(it)
//                showDialog = false
//            }
//        )
//    }
}
