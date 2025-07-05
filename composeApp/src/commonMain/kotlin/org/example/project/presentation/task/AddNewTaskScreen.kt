package org.example.project.presentation.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.datetime.LocalDate
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.CustomTextField
import org.example.project.presentation.base.CustomTextFieldTask
import org.example.project.presentation.base.theme.*
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.SpaceSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.feature.assignment.CalendarDialog


class AddNewTaskScreen :Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<TaskViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        AddNewTask(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}
@Composable
fun AddNewTask(
    navigator: Navigator?,
    state: TaskState,
    event: (TaskEvent) -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }


    var showDialogTime by remember { mutableStateOf(false) }

    val dateAnd = state.date?.let { reformattedToday(it) } ?: ""
    val timeAnd = state.time?.let { formatTime(it) } ?: ""


    LaunchedEffect(state.completed) {
        if (state.completed == true){
            navigator?.pop()
        }
    }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    if (showDialog) {
        CalendarDialog(
            selectedDate = selectedDate,
            onDismissRequest = { showDialog = false },
            onDateSelected = {
                selectedDate = it
                showDialog = false
            }
        )
    }


//    TimePickerDialog(
//        show = showDialogTime,
//        onTimeSelected = {
//            event(TaskEvent.OnTimeChange(it))
//        },
//        onDismiss = { showDialogTime = false },
//        currentTime = state.time
//    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {

        CustomHeader(
            title = stringResource(Res.string.yangi_vazifa_qo_shish),
            showBackButton = true,
            onBackClick = {
               navigator?.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CardCornerPadding)
        ) {
            Text(
                text = stringResource(Res.string.vazifa_nomi),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.W600,
            )

            SpaceMedium()

            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight)
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ContainerCornerRadius)),
                value = state.title,
                onValueChange = {
                    event(TaskEvent.OnTitleChange(it))
                },
                label = stringResource(Res.string.vazifa_nomi),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.note),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp)
                    )
                },
                fonSize = SmallTextSize,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                )
            )

            SpaceSmall()

            CustomTextFieldTask(
                value = state.desc,
                onValueChange = {
                    event(TaskEvent.OnDescChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ContainerCornerRadius))
                    .padding(vertical = 10.dp),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.task_square2),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = stringResource(Res.string.vazifa_haqida_qisqacha_ma_lumot),
                fonSize = SmallTextSize,
                minLine = true,
                singleLine = false

            )

            SpaceMedium()

            CustomTextField(
                onClick = { showDialog = true },
                value = dateAnd,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight)
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ContainerCornerRadius)),
                label = stringResource(Res.string.tugatish_sanasi),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.calendar_2),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp)
                    )
                },
                fonSize = SmallTextSize,
                readOnly = true,
            )

            SpaceSmall()

            CustomTextField(
                onClick = {
                    showDialogTime = true
                },
                value = formatTime(state.time),
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight)
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ContainerCornerRadius)),
                label = stringResource(Res.string.tugash_vaqti),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.alarm),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp)
                    )
                },
                fonSize = SmallTextSize,
                readOnly = true,
            )

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ContainerCornerRadius))
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                CustomButton(
                    fontSize = SmallTextSize,
                    modifier = Modifier
                        .weight(1f)
                        .height(DialogButtonHeight),
                    onClick = {
                        event(TaskEvent.OnImportanceChange(ImportanceType.MEDIUM))
                    },
                    text = stringResource(Res.string.o_rtacha),
                    color = if (state.importance == ImportanceType.MEDIUM || state.importance == ImportanceType.NONE) MediumButtonColor else Color.Transparent,
                    textColor = if (state.importance == ImportanceType.MEDIUM || state.importance == ImportanceType.NONE) Color.White else TextColor
                )
                CustomButton(
                    fontSize = SmallTextSize,
                    modifier = Modifier
                        .weight(1f)
                        .height(DialogButtonHeight),
                    onClick = {
                        event(TaskEvent.OnImportanceChange(ImportanceType.IMPORTANT))
                    },
                    text = stringResource(Res.string.muhim),
                    color = if (state.importance == ImportanceType.IMPORTANT || state.importance == ImportanceType.NONE) ImportantButtonColor else Color.Transparent,
                    textColor = TextColor
                )
                CustomButton(
                    fontSize = SmallTextSize,
                    modifier = Modifier
                        .weight(1f)
                        .height(DialogButtonHeight),
                    onClick = {
                        event(TaskEvent.OnImportanceChange(ImportanceType.MOST_IMPORTANT))
                    },
                    text = stringResource(Res.string.o_ta_muhim),
                    color = if (state.importance == ImportanceType.MOST_IMPORTANT || state.importance == ImportanceType.NONE) MostImportantButtonColor else Color.Transparent,
                    textColor = if (state.importance == ImportanceType.MOST_IMPORTANT || state.importance == ImportanceType.NONE) Color.White else TextColor
                )
            }

        }
        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            onClick = { },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = state.isSaveButtonEnabled,
            text = stringResource(Res.string.keyingisi)
        )

        SpaceLarge()
    }
}

@Preview
@Composable
private fun Preview() {
    AddNewTaskScreen()
}