package uz.tikoncha_parent.presentation.policy.rule_type_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bloklash_hafta_kunlari_soatlari
import tikoncha_parents.composeapp.generated.resources.bloklash_hudud_boyicha
import tikoncha_parents.composeapp.generated.resources.bloklash_shartlari
import tikoncha_parents.composeapp.generated.resources.bloklash_wifi_tarmogida
import tikoncha_parents.composeapp.generated.resources.chegaralash_ishlatish_marta
import tikoncha_parents.composeapp.generated.resources.chegaralash_kun_soat_va_daqiqa
import tikoncha_parents.composeapp.generated.resources.clock
import tikoncha_parents.composeapp.generated.resources.foydalanish_chegarasi
import tikoncha_parents.composeapp.generated.resources.icon_of
import tikoncha_parents.composeapp.generated.resources.ishga_tushirishlar_soni
import tikoncha_parents.composeapp.generated.resources.joylashuv
import tikoncha_parents.composeapp.generated.resources.locked
import tikoncha_parents.composeapp.generated.resources.permission_location
import tikoncha_parents.composeapp.generated.resources.qachon_va_qanday_holatlarda_ilova_va_veb
import tikoncha_parents.composeapp.generated.resources.shartlar
import tikoncha_parents.composeapp.generated.resources.vaqt
import tikoncha_parents.composeapp.generated.resources.wi_fi
import tikoncha_parents.composeapp.generated.resources.wi_fi as wiFiLabel
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleListScreen
import uz.tikoncha_parent.presentation.policy.location_rule.LocationRuleScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleListScreen
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

class RuleTypeSelectionScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

        RuleTypeSelectionUi(
            navigator = navigator,
            state = sharedState,
        )
    }
}

@Composable
fun RuleTypeSelectionUi(
    navigator: Navigator?,
    state: PolicySharedState = PolicySharedState(),
) {
    val ruleTypes = listOf(
        RuleTypeUi(
            type = RuleType.TIME,
            icon = painterResource(Res.drawable.clock),
            title = stringResource(Res.string.vaqt),
            subtitle = stringResource(Res.string.bloklash_hafta_kunlari_soatlari),
            enabled = true,
            hasItems = state.timeList.isNotEmpty(),
            soon = false,
        ),
        RuleTypeUi(
            type = RuleType.USAGE_LIMIT,
            icon = painterResource(Res.drawable.locked),
            title = stringResource(Res.string.foydalanish_chegarasi),
            subtitle = stringResource(Res.string.chegaralash_kun_soat_va_daqiqa),
            enabled = true,
            hasItems = state.limitList.isNotEmpty(),
            soon = false,
        ),
        RuleTypeUi(
            type = RuleType.LOCATION,
            icon = painterResource(Res.drawable.permission_location),
            title = stringResource(Res.string.joylashuv),
            subtitle = stringResource(Res.string.bloklash_hudud_boyicha),
            enabled = true,
            hasItems = state.locationRule != null,
            soon = false,
        ),
        RuleTypeUi(
            type = RuleType.WIFI,
            icon = painterResource(Res.drawable.wi_fi),
            title = stringResource(Res.string.wi_fi),
            subtitle = stringResource(Res.string.bloklash_wifi_tarmogida),
            enabled = false,
            hasItems = false,
            soon = true,
        ),
        RuleTypeUi(
            type = RuleType.LAUNCH_COUNT,
            icon = painterResource(Res.drawable.icon_of),
            title = stringResource(Res.string.ishga_tushirishlar_soni),
            subtitle = stringResource(Res.string.chegaralash_ishlatish_marta),
            enabled = false,
            hasItems = false,
            soon = true,
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.bg.secondary),
    ) {
        CustomHeader(
            title = stringResource(Res.string.shartlar),
            showBackButton = true,
            onBackClick = { navigator?.pop() },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Space(12.dp)

            Text(
                text = stringResource(Res.string.bloklash_shartlari),
                color = AppColors.text.primary,
                style = AppTypography.headlineMdSemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ContainerPadding),
            )

            Space(12.dp)

            Text(
                text = stringResource(Res.string.qachon_va_qanday_holatlarda_ilova_va_veb),
                color = AppColors.text.secondary,
                style = AppTypography.emphasizedMdMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Space(4.dp)

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 24.dp),
            ) {
                items(items = ruleTypes, key = { it.type }) { item ->
                    RuleTypeItem(
                        ruleTypeUi = item,
                        onClick = {
                            if (!item.enabled || item.soon) return@RuleTypeItem
                            navigateForType(navigator, item.type)
                        },
                    )
                }
            }
        }
    }
}

private fun navigateForType(navigator: Navigator?, type: RuleType) {
    when (type) {
        RuleType.TIME -> navigator?.push(TimeRuleListScreen())
        RuleType.USAGE_LIMIT -> navigator?.push(LimitRuleListScreen())
        RuleType.LOCATION -> navigator?.push(LocationRuleScreen())
        RuleType.WIFI,
        RuleType.LAUNCH_COUNT -> Unit // soon — hech narsa qilinmaydi
        RuleType.NONE -> {}
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        RuleTypeSelectionUi(
            navigator = null,
            state = PolicySharedState(),
        )
    }
}