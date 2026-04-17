package uz.tikoncha_parent.presentation.policy.limit_rule.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.close_remove
import tikoncha_parents.composeapp.generated.resources.kunlik
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.soatlik
import uz.example.wheeltimepicker.WheelTimePicker
import uz.example.wheeltimepicker.WheelTimePickerDefaults
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.PillSegmentedButton
import uz.tikoncha_parent.presentation.base.PillSegmentedItem
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.policy.common.WeekdayChips
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.time_rule.setup.TimeRuleSetupEvent
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

class LimitRuleSetupScreen(
    private val ruleId: Int? = null,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val viewModel = koinScreenModel<LimitRuleSetupViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        // ── Init bir marta ─────────────────────
        LaunchedEffect(Unit) {
            val editing = ruleId?.let { id -> sharedState.limitList.find { it.id == id } }
            event(
                LimitRuleSetupEvent.Init(
                    editingRule = editing,
                    allLimitRules = sharedState.limitList,
                )
            )
        }

        // ── Effect → shared + pop ──────────────
        LaunchedEffect(Unit) {
            viewModel.effect.collect { eff ->
                when (eff) {
                    is LimitRuleSetupEffect.Saved -> {
                        sharedEvent(PolicySharedEvent.UpsertLimitRule(eff.rule))
                        navigator?.pop()
                    }
                }
            }
        }

        // ── UI — o'zingiz yozasiz ──────────────
        LimitRuleSetupUi(
            state = state,
            event = event,
            canUpdate = sharedState.canUpdate,
            onBack = { navigator?.pop() },
        )
    }
}

@Composable
fun LimitRuleSetupUi(
    state: LimitRuleSetupState,
    event: (LimitRuleSetupEvent) -> Unit,
    canUpdate: Boolean,
    onBack: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    event(LimitRuleSetupEvent.SelectDay(day))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp)
            )
            Space(24.dp)
            PillSegmentedButton(
                items = listOf(
                    PillSegmentedItem(stringResource(DayHour.DAY.resId)),
                    PillSegmentedItem(stringResource(DayHour.HOUR.resId)),
                ),
                selectedIndex = if (state.limitType == DayHour.DAY) 0 else 1,
                onSelected = { index ->
                    val type = if (index == 0) DayHour.DAY else DayHour.HOUR
                    event(LimitRuleSetupEvent.SelectLimitType(type))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
            Space(24.dp)
            WheelTimePicker(
                initialMinute = state.duration.toMinutes(),
                onTimeChanged = { hour, minute ->
                    event(LimitRuleSetupEvent.SetHour(hour))
                    event(LimitRuleSetupEvent.SetMinute(minute))
                },
                colors = WheelTimePickerDefaults.colors(),
                showHours = state.limitType == DayHour.DAY,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
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
                enabled = state.canSave,
                text = stringResource(Res.string.saqlash),
                onClick = {
                    event(LimitRuleSetupEvent.Save)
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
fun Pre(){
    TikonchaParentTheme (mode = ThemeMode.LIGHT){
        LimitRuleSetupUi(
            state = LimitRuleSetupState(
                limitType = DayHour.HOUR
            ),
            event = {},
            canUpdate = true,
            onBack = {},
        )
    }
}