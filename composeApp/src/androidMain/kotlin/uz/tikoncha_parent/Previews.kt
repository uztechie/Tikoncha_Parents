package uz.tikoncha_parent

import TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import io.ktor.client.request.invoke
import kotlinx.coroutines.launch

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import tikoncha_parents.composeapp.generated.resources.arrow_down
import tikoncha_parents.composeapp.generated.resources.arrow_up
import tikoncha_parents.composeapp.generated.resources.bloklamoqchi_bo_lgan_ilova_yoki_saytlarni_tanlang
import tikoncha_parents.composeapp.generated.resources.bloklash_shartlari
import tikoncha_parents.composeapp.generated.resources.clock
import tikoncha_parents.composeapp.generated.resources.davom_etish
import tikoncha_parents.composeapp.generated.resources.edite_pen_ilne
import tikoncha_parents.composeapp.generated.resources.farzand_qoshish
import tikoncha_parents.composeapp.generated.resources.farzandingizni_tasdiqlang
import tikoncha_parents.composeapp.generated.resources.ish_vaqti_dam_olish_kuni
import tikoncha_parents.composeapp.generated.resources.jadvallar
import tikoncha_parents.composeapp.generated.resources.password_check
import tikoncha_parents.composeapp.generated.resources.profil
import tikoncha_parents.composeapp.generated.resources.qachon_va_qayerda
import tikoncha_parents.composeapp.generated.resources.qora_ro_yxat
import tikoncha_parents.composeapp.generated.resources.qr_screen
import tikoncha_parents.composeapp.generated.resources.search
import tikoncha_parents.composeapp.generated.resources.search_normal
import tikoncha_parents.composeapp.generated.resources.shartlar
import tikoncha_parents.composeapp.generated.resources.ulandi
import tikoncha_parents.composeapp.generated.resources.ushbu_kodni_farzandingiz_telefonidan_kiriting
import tikoncha_parents.composeapp.generated.resources.vaqt
import tikoncha_parents.composeapp.generated.resources.vazifa_qo_shish
import tikoncha_parents.composeapp.generated.resources.wi_fi
import tikoncha_parents.composeapp.generated.resources.wi_fi_tarmoqlarni_tanlang
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.assignment.CalendarDialog
import uz.saidburxon.newedu.presentation.feature.assignment.reformattedYearDay
import uz.saidburxon.newedu.presentation.feature.main.MainScreen
import uz.tikoncha_parent.common.Util
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.base.CustomTextFieldTask
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.presentation.home.schedule.RoundedCheckbox
import uz.tikoncha_parent.presentation.monitoring.items
import uz.tikoncha_parent.presentation.profile.coins.CoinAmountTextField
import uz.tikoncha_parent.presentation.profile.subscription.PaymentScreen
import uz.tikoncha_parent.presentation.task.ImportanceType
import uz.tikoncha_parent.presentation.task.TaskEvent
import uz.tikoncha_parent.presentation.task.TaskState
import uz.tikoncha_parent.presentation.task.formatTime
import uz.tikoncha_parent.presentation.task.reformattedToday
import uz.tikoncha_parent.presentation.task.rememberHideKeyboard
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.BackgroundColor
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.CardColors
import uz.tikoncha_parent.ui.CardCornerPadding
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.DisableTextColor
import uz.tikoncha_parent.ui.HeaderHeight
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.ImportantButtonColor
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.LightGrayColor
import uz.tikoncha_parent.ui.MediumButtonColor
import uz.tikoncha_parent.ui.MostImportantButtonColor
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SliderPageColor
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextColor
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.UltraLargeTextSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.io.path.Path
import kotlin.io.path.moveTo


@Composable
fun ScheduleTypeUi(
    navigator: Navigator?,
    state: TaskState,
    event: (TaskEvent) -> Unit,
) {

    val navigator: Navigator?


    var coinsAmount by remember { mutableStateOf("0") }
    val maxAvailable = 50
    val remaining = (maxAvailable - (coinsAmount.toIntOrNull() ?: 0)).coerceAtLeast(0)

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
        onDismiss = { showTaskErrorDialog = false },
        show = showTaskErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = taskErrorText,
        onButtonClick = {
            showTaskErrorDialog = false
        }
    )

    if (showTaskSuccessDialog) {
        CustomDialog(
            title = stringResource(Res.string.muvaffaqiyatli),
            message = successMessage,
            onDismiss = { showTaskSuccessDialog = false },
            onButtonClick = {
//                navigator?.pop()
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
//            navigator?.pop()
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
            initialTime = selectedTime ?: Util.getCurrentTime(),
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
            .pointerInput(Unit) {
                detectTapGestures(onTap = { hidKeyboard() })
            }
    ) {

        CustomHeader(
            title = stringResource(Res.string.yangi_vazifa_qo_shish),
            showBackButton = true,
            onBackClick = {
//                navigator?.pop()
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
                    .border(
                        1.dp,
                        MaterialTheme.extendedColor.borderColor,
                        RoundedCornerShape(TextFieldCornerRadius)
                    ),
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
                    .border(
                        1.dp,
                        MaterialTheme.extendedColor.borderColor,
                        RoundedCornerShape(TextFieldCornerRadius)
                    )
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
                    .border(
                        1.dp,
                        MaterialTheme.extendedColor.borderColor,
                        RoundedCornerShape(TextFieldCornerRadius)
                    )
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
                    textColor = if (state.importance == ImportanceType.IMPORTANT || state.importance == ImportanceType.NONE) OnPrimaryColor else HintTextColor
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

            SpaceLarge()

            CustomText(
                text = stringResource(Res.string.tangachalar_sovg_a_qiling),
                fontSize = NormalLargeTextSize,
                fontWeight = FontWeight.W600
            )

            SpaceMedium()

            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomText(
                    text = stringResource(Res.string.sizda_mavjud_tangachalar),
                    fontSize = NormalLargeTextSize,
                )
                SpaceSmall()
                Text(text = "$remaining ${stringResource(Res.string.ta)}")
            }

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(SmallIconButtonSize)
                        .clip(RoundedCornerShape(ShapeCornerRadius))
                        .background(MaterialTheme.extendedColor.cardColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.coin),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(0.7f)
                    )
                }

                SpaceUltraSmall()

                CustomText(
                    text = stringResource(Res.string.tangachalar),
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                CoinAmountTextField(
                    coinsAmount = coinsAmount,
                    onValueChange = {
                        coinsAmount = it
                    },
                    onAddCoinClicked = {
                        coinsAmount = it.toString()
                    },
                    onSubtractButtonClicked = {
                        coinsAmount = it.toString()
                    }
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
    TikonchaParentTheme(ThemeMode.LIGHT) {
        ScheduleTypeUi(
            navigator = null,
            state = TaskState(),
            event = {}
        )
    }
}