package uz.tikoncha_parent.presentation.policy.time_rule.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bed_sleeping
import tikoncha_parents.composeapp.generated.resources.cheklov_vaqti
import tikoncha_parents.composeapp.generated.resources.close_remove
import tikoncha_parents.composeapp.generated.resources.dot
import tikoncha_parents.composeapp.generated.resources.faqat_shu_vaqtda_ishlasin
import tikoncha_parents.composeapp.generated.resources.kerakli_hafta_kunlarini_tanlang
import tikoncha_parents.composeapp.generated.resources.kerakli_vaqt_oralig_ini_tanlang
import tikoncha_parents.composeapp.generated.resources.kun_davomida
import tikoncha_parents.composeapp.generated.resources.sand_time_policy
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.time_icon
import tikoncha_parents.composeapp.generated.resources.timer
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomSwitch
import uz.tikoncha_parent.presentation.base.LocalToastHost
import uz.tikoncha_parent.presentation.base.WheelTimePickerDialog
import uz.tikoncha_parent.presentation.base.TimeRangePicker
import uz.tikoncha_parent.presentation.base.TimeRangePickerDefaults
import uz.tikoncha_parent.presentation.base.ToastData
import uz.tikoncha_parent.presentation.base.ToastProvider
import uz.tikoncha_parent.presentation.base.ToastType
import uz.tikoncha_parent.presentation.policy.app_site_selection.AppCheckbox
import uz.tikoncha_parent.presentation.policy.common.WeekdayChips
import uz.tikoncha_parent.presentation.policy.common.formatDuration
import uz.tikoncha_parent.presentation.policy.common.toHhMm
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class TimeRuleSetupScreen(
    private val ruleId: Int? = null,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val viewModel = koinScreenModel<TimeRuleSetupViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        // ── Init bir marta ─────────────────────
        LaunchedEffect(Unit) {
            val editing = ruleId?.let { id -> sharedState.timeList.find { it.id == id } }
            event(
                TimeRuleSetupEvent.Init(
                    editingRule = editing,
                    allTimeRules = sharedState.timeList,
                )
            )
        }


        ToastProvider {
            TimeRuleSetupUi(
                state = state,
                event = event,
                effect = viewModel.effect,
                sharedEvent = sharedEvent,
                canUpdate = sharedState.canUpdate,
                onBack = { navigator?.pop() },
            )
        }
    }
}

@Composable
fun TimeRuleSetupUi(
    state: TimeRuleSetupState,
    event: (TimeRuleSetupEvent) -> Unit,
    effect: Flow<TimeRuleSetupEffect>,
    sharedEvent: (PolicySharedEvent) -> Unit,
    canUpdate: Boolean,
    onBack: () -> Unit,
) {

    var showStartTimePickerDialog by remember { mutableStateOf(false) }
    var showEndTimePickerDialog by remember { mutableStateOf(false) }

    val noWeekDaySelected = stringResource(Res.string.kerakli_hafta_kunlarini_tanlang)
    val noTimeIntervalSelected = stringResource(Res.string.kerakli_vaqt_oralig_ini_tanlang)
    val toast = LocalToastHost.current

    // ── Effect → shared + pop ──────────────
    LaunchedEffect(Unit) {
        effect.collect { eff ->
            when (eff) {
                is TimeRuleSetupEffect.Saved -> {
                    sharedEvent(PolicySharedEvent.UpsertTimeRule(eff.rule))
                    onBack()
                }

                TimeRuleSetupEffect.NoDaySelectionToast -> {
                    toast.show(
                        toast = ToastData(
                            type = ToastType.Warning,
                            title = noWeekDaySelected
                        )
                    )
                }

                TimeRuleSetupEffect.NoTimeIntervalSelectionToast -> {
                    toast.show(
                        toast = ToastData(
                            type = ToastType.Warning,
                            title = noTimeIntervalSelected
                        )
                    )
                }
            }
        }
    }

    WheelTimePickerDialog(
        show = showStartTimePickerDialog,
        currentTime = state.startTime,
        onDismiss = {
            showStartTimePickerDialog = false
        },
        onConfirm = {
            event(TimeRuleSetupEvent.SetStartTime(it))
            showStartTimePickerDialog = false
        }
    )
    WheelTimePickerDialog(
        show = showEndTimePickerDialog,
        currentTime = state.endTime,
        onDismiss = {
            showEndTimePickerDialog = false
        },
        onConfirm = {
            event(TimeRuleSetupEvent.SetEndTime(it))
            showEndTimePickerDialog = false
        }
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                    .size(40.dp),

                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppColors.section.secondary,
                    contentColor = AppColors.icon.primary
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.close_remove),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                )
            }

            WeekdayChips(
                chips = state.weekDays,
                onToggle = { day ->
                    event(TimeRuleSetupEvent.SelectDay(day))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp)
            )
            Space(24.dp)
            TimeRangePicker(
                enabled = !state.allDay,
                start = state.startTime,
                end = state.endTime,
                stepMinutes = 10,
                onTimeChange = { start, end ->
                    event(TimeRuleSetupEvent.SetTimeRange(start, end))
                },
                reverse = state.reverse,
                colors = TimeRangePickerDefaults.colors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                startIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.dot),
                        contentDescription = "",
                        tint = AppColors.icon.inverse,
                        modifier = Modifier
                            .size(10.dp)
                    )
                },
                endIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.dot),
                        contentDescription = "",
                        tint = AppColors.icon.inverse,
                        modifier = Modifier
                            .size(10.dp)
                    )
                },
                centerContent = {
                    Image(
                        painter = painterResource(Res.drawable.sand_time_policy),
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp)
                    )
                }

            )

            Space(24.dp)
            Row(
                modifier = Modifier
                    .height(44.dp)
                    .background(AppColors.bg.primaryContainer, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        enabled = !state.allDay,
                        onClick = {
                            event(TimeRuleSetupEvent.SetReverse(!state.reverse))
                        }
                    )
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppCheckbox(
                    checked = state.reverse,
                    onCheckedChange = {
                        if (!state.allDay){
                            event(TimeRuleSetupEvent.SetReverse(it))
                        }
                    }
                )
                Space(10.dp)
                Text(
                    text = stringResource(Res.string.faqat_shu_vaqtda_ishlasin),
                    style = AppTypography.titleMdSemiBold,
                    color = AppColors.text.primary
                )
            }

            Space(36.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TimeRuleSetupTimeCard(
                    value = state.startTime.toHhMm(),
                    onClick = {
                        if (!state.allDay){
                            showStartTimePickerDialog = true
                        }
                    }
                )
                Space(16.dp)
                Text(
                    text = ":",
                    style = AppTypography.headlineLgSemiBold,
                    color = AppColors.icon.secondary
                )
                Space(16.dp)
                TimeRuleSetupTimeCard(
                    value = state.endTime.toHhMm(),
                    onClick = {
                        if (!state.allDay){
                            showEndTimePickerDialog = true
                        }
                    }
                )

            }

            Space(12.dp)
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "${stringResource(Res.string.cheklov_vaqti)} : ${formatDuration(state.perDayMinutes)}",
                style = AppTypography.titleSmSemiBold,
                color = AppColors.text.primary
            )

            Space(24.dp)

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .background(AppColors.bg.surface, RoundedCornerShape(20.dp))
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = {
                            event(TimeRuleSetupEvent.SetAllDay(!state.allDay))
                        }
                    )
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.time_icon),
                    contentDescription = "",
                    tint = AppColors.icon.accentPrimary,
                    modifier = Modifier
                        .size(24.dp)
                )
                Space(8.dp)
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(Res.string.kun_davomida),
                    style = AppTypography.titleMdMedium,
                    color = AppColors.text.primary
                )

                CustomSwitch(
                    checked = state.allDay,
                    onCheckedChange = {
                        event(TimeRuleSetupEvent.SetAllDay(it))
                    }
                )

            }
            Space(24.dp)


        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    AppColors.bg.elevated,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            CustomButtonNew(
                text = stringResource(Res.string.saqlash),
                onClick = {
                    event(TimeRuleSetupEvent.Save)
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }

}


@Composable
fun TimeRuleSetupTimeCard(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(AppColors.button.surface, RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onClick
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = AppTypography.headlineLgSemiBold,
            color = AppColors.text.accentEmphasis
        )
    }
}


@Preview
@Composable
fun Pre() {
    TikonchaParentTheme {
        TimeRuleSetupUi(
            state = TimeRuleSetupState(
                allDay = true
            ),
            event = {},
            sharedEvent = {},
            effect = emptyFlow(),
            canUpdate = true,
            onBack = {},
        )
    }
}