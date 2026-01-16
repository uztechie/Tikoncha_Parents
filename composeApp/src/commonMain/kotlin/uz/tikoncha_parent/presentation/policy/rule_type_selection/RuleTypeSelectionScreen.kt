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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleListScreen
import uz.tikoncha_parent.presentation.policy.location_rule.LocationRuleScreen
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupUi
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleListScreen
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class RuleTypeSelectionScreen() : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val state by sharedViewModel.state.collectAsStateWithLifecycle()
        val event = sharedViewModel::onEvent

        RuleTypeSelectionUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}


@Composable
fun RuleTypeSelectionUi(
    navigator: Navigator?,
    state: PolicySharedState = PolicySharedState(),
    event: (PolicySharedEvent) -> Unit = {}
) {

    var showSubscriptionLimitDialog by remember {
        mutableStateOf(false)
    }


    var showWarningDialog by remember {
        mutableStateOf(false)
    }

    var selectedRuleType by remember {
        mutableStateOf(RuleType.NONE)
    }


    CustomDialog(
        showCloseButton = true,
        painter = painterResource(Res.drawable.dialog_subscription),
        show = showSubscriptionLimitDialog,
        title = stringResource(Res.string.obuna),
        message = stringResource(Res.string.obuna_dialog_message),
        buttonText = stringResource(Res.string.obuna_bolish),
        onDismiss = {
            showSubscriptionLimitDialog = false
        },
        onButtonClick = {
            showSubscriptionLimitDialog = false
            navigator?.push(
                SubscriptionPaymentScreen(
                    selectedChild = state.selectedChild
                )
            )

        }
    )


    CustomDialog(
        painter = painterResource(Res.drawable.dialog_info),
        show = showWarningDialog,
        title = stringResource(Res.string.diqqat),
        message = stringResource(Res.string.siz_tanlagan_vaqt_oraligida),
        onDismiss = {
            showWarningDialog = false
        },
        onButtonClick = {
            showWarningDialog = false
            when(selectedRuleType){
                RuleType.TIME -> {
                    if (state.subscriptionLimit.timeRule < 1){
                        showSubscriptionLimitDialog = true
                    }else{
                        navigator?.push(TimeRuleListScreen())
                    }
                }
                RuleType.USAGE_LIMIT -> {
                    if (state.subscriptionLimit.limitRule < 1){
                        showSubscriptionLimitDialog = true
                    }else{
                        navigator?.push(LimitRuleListScreen())
                    }

                }
                RuleType.LOCATION -> {
                    if (state.subscriptionLimit.locationRule < 1){
                        showSubscriptionLimitDialog = true
                    }else{
                        navigator?.push(LocationRuleScreen())
                    }
                }
               else -> {}
            }
        }
    )


    val scheduleList = listOf(
        RuleTypeUi(
            type = RuleType.TIME,
            icon = painterResource(Res.drawable.clock),
            title = stringResource(Res.string.vaqt),
            subtitle = stringResource(Res.string.bloklash_hafta_kunlari_soatlari),
            enabled = state.timeList.isEmpty(),
            hasItems = state.timeList.isNotEmpty(),
            soon = false
        ),
        RuleTypeUi(
            type = RuleType.USAGE_LIMIT,
            icon = painterResource(Res.drawable.time_limit),
            title = stringResource(Res.string.foydalanish_chegarasi),
            subtitle = stringResource(Res.string.chegaralash_kun_soat_va_daqiqa),
            enabled = state.limitList.isEmpty(),
            hasItems = state.limitList.isNotEmpty(),
            soon = false
        ),
        RuleTypeUi(
            type = RuleType.LOCATION,
            icon = painterResource(Res.drawable.permission_location),
            title = stringResource(Res.string.joylashuv),
            subtitle = stringResource(Res.string.bloklash_hudud_boyicha),
            enabled = state.locationRule == null,
            hasItems = state.locationRule != null,
            soon = false
        ),
        RuleTypeUi(
            type = RuleType.WIFI,
            icon = painterResource(Res.drawable.wi_fi),
            title = stringResource(Res.string.wi_fi),
            subtitle = stringResource(Res.string.bloklash_wifi_tarmogida),
            enabled = false,
            hasItems = false,
            soon = true
        ),
        RuleTypeUi(
            type = RuleType.LAUNCH_COUNT,
            icon = painterResource(Res.drawable.icon_of),
            title = stringResource(Res.string.ishga_tushirishlar_soni),
            subtitle = stringResource(Res.string.chegaralash_ishlatish_marta),
            enabled = false,
            hasItems = false,
            soon = true
        ),

    )

    var showSetupDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.jadval),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            CustomText(
                text = stringResource(Res.string.bloklash_shartlari),
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    horizontal = ContainerPadding
                )
            )

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.qachon_va_qayerda),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
                color = HintTextColor,
                modifier = Modifier.padding(
                    horizontal = ContainerPadding
                )
            )

            SpaceMedium()
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    ContainerPadding
                ),
                content = {
                    items(items = scheduleList, key = { it.type }) { item ->


                        RuleTypeItem(
                            ruleTypeUi = item,
                            onClick = {

                                selectedRuleType = item.type

                                when(item.type){
                                    RuleType.TIME -> {
                                        if (state.limitList.isNotEmpty()){
                                            showWarningDialog = true
                                            return@RuleTypeItem
                                        }
                                        if (state.subscriptionLimit.timeRule < 1){
                                            showSubscriptionLimitDialog = true
                                        }else{
                                            navigator?.push(TimeRuleListScreen())
                                        }
                                    }
                                    RuleType.USAGE_LIMIT -> {
                                        if (state.timeList.isNotEmpty()){
                                            showWarningDialog = true
                                            return@RuleTypeItem
                                        }
                                        if (state.subscriptionLimit.limitRule < 1){
                                            showSubscriptionLimitDialog = true
                                        }else{
                                            navigator?.push(LimitRuleListScreen())
                                        }
                                    }

                                    RuleType.LOCATION -> {
                                        if (state.subscriptionLimit.locationRule < 1){
                                            showSubscriptionLimitDialog = true
                                        }else{
                                            navigator?.push(LocationRuleScreen())
                                        }
                                    }
                                    RuleType.WIFI -> {}
                                    RuleType.LAUNCH_COUNT -> {}


                                    else -> {

                                    }
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        RuleTypeSelectionUi(
            navigator = null,
        )
    }
}