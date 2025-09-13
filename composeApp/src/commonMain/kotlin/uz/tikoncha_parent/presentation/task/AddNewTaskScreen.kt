package uz.tikoncha_parent.presentation.task

import TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.common.Util
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.base.CustomTextFieldTask
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.assignment.CalendarDialog
import uz.saidburxon.newedu.presentation.feature.assignment.reformattedYearDay
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.extendedColor


class AddNewTaskScreen(
    private val taskToEdit: Task? = null
) : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<TaskViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(taskToEdit) {
            taskToEdit?.let { event(TaskEvent.OnEditTask(it)) }
        }

        val navigator = LocalNavigator.current

        AddNewTask(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTask(
    navigator: Navigator?,
    state: TaskState,
    event: (TaskEvent) -> Unit,
) {

    val hidKeyboard = rememberHideKeyboard()

    var selectedDate by remember { mutableStateOf(state.date) }
    var selectedTime by remember { mutableStateOf(state.time) }
    val dateText = state.date?.let { reformattedYearDay(it) } ?: ""
    val timeText = state.time?.let { formatTime(it) } ?: ""


    var showDialogData by remember { mutableStateOf(false) }
    var showDialogTime by remember { mutableStateOf(false) }
    val timeAnd = state.time?.let { formatTime(it) } ?: ""
    val dateAnd = state.date?.let { reformattedToday(it) } ?: ""

    val onClick = state.title.isNotBlank() &&
            state.desc.isNotBlank() &&
            state.date != null &&
            state.time != null &&
            state.importance != ImportanceType.NONE

    var showTaskSuccessDialog by remember { mutableStateOf(false) }

    val successMessage = if (state.isEditing)
        stringResource(Res.string.vazifa_tahrirlandi)
    else
        stringResource(Res.string.yangi_vazifa_yaratildi)

    val taskLoading = state.taskResponseState is ResponseState.Loading
    val taskErrorText = state.taskResponseState.errorText()
    val taskSuccess = state.taskResponseState is ResponseState.Success

    LoadingDialog(taskLoading)
    var showTaskErrorDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(taskErrorText) {
        if (taskErrorText.isNotEmpty()) {
            showTaskErrorDialog = true
        }
    }

    CustomDialog(
        onDismiss = {showTaskErrorDialog = false},
        show = showTaskErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = taskErrorText,
        onButtonClick = {
            showTaskErrorDialog = false
        }
    )

    if (showTaskSuccessDialog){
        CustomDialog(
            title = stringResource(Res.string.muvaffaqiyatli),
            message = successMessage,
            onDismiss = { showTaskSuccessDialog = false},
            onButtonClick = {
                navigator?.pop()
                showTaskSuccessDialog = false
            }
        )
    }

    LaunchedEffect(taskSuccess) {
        if (taskSuccess) {
            showTaskSuccessDialog = true
            event.invoke(TaskEvent.OnReset)
        }
    }

    LaunchedEffect(state.completed) {
        if (state.completed == true) {
            navigator?.pop()
        }
    }

    if (showDialogData) {
        CalendarDialog(
            selectedDate = selectedDate,
            onDismissRequest = { showDialogData = false },
            onDateSelected = {
                println("AAAA = $dateAnd")
                event(TaskEvent.OnDateChange(it))
                showDialogData = false
            }
        )
    }


    if (showDialogTime) {
        TimePickerDialog(
            show = showDialogTime,
            initialTime = selectedTime ?: Util.getCurrentTime(), // helper function
            onDismiss = { showDialogTime = false },
            onTimeSelected = {
                println("AAAA = $timeAnd")
                event(TaskEvent.OnTimeChange(it))
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit){
                detectTapGestures(onTap =  { hidKeyboard() })
            }
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
            CustomText(
                text = stringResource(Res.string.vazifa_nomi),
                fontSize = NormalLargeTextSize,
                fontWeight = FontWeight.W600,
            )

            SpaceMedium()

            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight)
                    .border(1.dp, MaterialTheme.extendedColor.borderColor, RoundedCornerShape(TextFieldCornerRadius)),
                value = state.title,
                onValueChange = {
                    event(TaskEvent.OnTitleChange(it))
                },
                label = stringResource(Res.string.vazifa_nomi),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.note),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp),
                        colorFilter = ColorFilter.tint(SliderPageColor)
                    )
                },
                fonSize = SmallTextSize,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
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
                    .border(1.dp, MaterialTheme.extendedColor.borderColor, RoundedCornerShape(TextFieldCornerRadius))
                    .padding(vertical = 10.dp),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.task_square2),
                        contentDescription = "",
                        modifier = Modifier.size(22.dp),
                        colorFilter = ColorFilter.tint(SliderPageColor)
                    )
                },
                label = stringResource(Res.string.vazifa_haqida_qisqacha_ma_lumot),
                fonSize = SmallTextSize,
                minLine = true,
                singleLine = false
            )

            SpaceMedium()

            CustomSelectionButton(
                text = dateText,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                label = stringResource(Res.string.tugatish_sanasi),
                painter = painterResource(Res.drawable.calendar_2),
                onClick = {
                    showDialogData = true
                },
                showTrailingIcon = false,
                fontWeight = FontWeight.W500,
                fonSize = SmallTextSize
            )

            SpaceSmall()

            CustomSelectionButton(
                text = timeText,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                label = stringResource(Res.string.tugash_vaqti),
                painter = painterResource(Res.drawable.alarm),
                onClick = {
                    showDialogTime = true
                },
                showTrailingIcon = false,
                fontWeight = FontWeight.W500,
                fonSize = SmallTextSize
            )

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.extendedColor.borderColor, RoundedCornerShape(TextFieldCornerRadius))
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
                    textColor = if (state.importance == ImportanceType.MEDIUM || state.importance == ImportanceType.NONE) OnPrimaryColor else HintTextColor
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
                    textColor = if(state.importance == ImportanceType.IMPORTANT || state.importance == ImportanceType.NONE) OnPrimaryColor else HintTextColor
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
                    textColor = if (state.importance == ImportanceType.MOST_IMPORTANT || state.importance == ImportanceType.NONE) OnPrimaryColor else HintTextColor
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            onClick = {
                event(TaskEvent.OnConfirmClicked)
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = onClick,
            text = stringResource(Res.string.saqlash)
        )
    }
}


@Preview
@Composable
private fun Preview() {
    AddNewTask(
        navigator = null,
        state = TaskState(),
        event = {}
    )
}