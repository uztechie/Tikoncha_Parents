package uz.tikoncha_parent.presentation.policy.limit_rule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.faol_vaqtni_qoshing
import tikoncha_parents.composeapp.generated.resources.foydalanish_limiti
import tikoncha_parents.composeapp.generated.resources.limit_qoshish
import tikoncha_parents.composeapp.generated.resources.limit_yoq
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.sizda_faol_vaqt_yoq
import tikoncha_parents.composeapp.generated.resources.time_large_icon
import tikoncha_parents.composeapp.generated.resources.web_ilova_limit
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.base.CustomButtonDash
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.policy.limit_rule.setup.LimitRuleSetupScreen
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class LimitRuleListScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        LimitRuleListUi(
            rules = sharedState.limitList,
            canUpdate = sharedState.canUpdate,
            canSave = sharedState.canSavePolicy,
            onBack = { navigator?.pop() },
            onAdd = {
                if (sharedState.canUpdate) {
                    navigator?.push(LimitRuleSetupScreen(ruleId = null))
                }
            },
            onItemClick = { rule ->
                if (sharedState.canUpdate) {
                    navigator?.push(LimitRuleSetupScreen(ruleId = rule.id))
                }
            },
            onDelete = { id ->
                sharedEvent(PolicySharedEvent.RemoveLimitRule(id))
            },
            onDone = {
                navigator?.popUntil { it is PolicySetupScreen }
            },
        )
    }
}

@Composable
fun LimitRuleListUi(
    rules: List<LimitRuleUi>,
    canUpdate: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onItemClick: (LimitRuleUi) -> Unit,
    onDelete: (id: Int) -> Unit,
    onDone: () -> Unit,
) {
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
        CustomHeader(
            title = stringResource(Res.string.foydalanish_limiti),
            showBackButton = true,
            onBackClick = onBack
        )

        if (rules.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                Image(
                    painter = painterResource(Res.drawable.time_large_icon),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                )
                Space(27.dp)
                Text(
                    text = stringResource(Res.string.limit_yoq),
                    style = AppTypography.titleMdMedium,
                    color = AppColors.text.primary,
                    textAlign = TextAlign.Center,
                )
                Space(12.dp)

                Text(
                    text = stringResource(Res.string.web_ilova_limit),
                    style = AppTypography.titleSmMedium,
                    color = AppColors.text.placeholder,
                    textAlign = TextAlign.Center,
                )

                if (canUpdate) {
                    Space(8.dp)
                    CustomButtonNew(
                        text = stringResource(Res.string.limit_qoshish),
                        onClick = onAdd,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = rules,
                    key = { "${it.id}:${it.weekDays}:${it.time}" }
                ) {
                    LimitRuleItem(
                        item = it,
                        onClick = { onItemClick(it) },
                        onRemove = { onDelete(it.id) },
                        canRemove = canUpdate
                    )
                }
                item {
                    if (rules.isNotEmpty()) {
                        CustomButtonDash(
                            text = stringResource(Res.string.limit_qoshish),
                            onClick = onAdd,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                }
            }

            if (canUpdate) {
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
                        onClick = onDone,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        LimitRuleListUi(
            rules = listOf(
                LimitRuleUi(
                    id = 1,
                    limitType = DayHour.DAY,
                    time = HourMinute(12, 0),
                    weekDays = setOf(WeekDay.FRI, WeekDay.MON)
                )
            ),
            canUpdate = false,
            canSave = true,
            onBack = {},
            onAdd = {},
            onItemClick = {},
            onDelete = {},
            onDone = {},
        )
    }
}