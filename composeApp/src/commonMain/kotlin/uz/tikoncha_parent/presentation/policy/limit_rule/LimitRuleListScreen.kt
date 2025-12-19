package uz.tikoncha_parent.presentation.policy.limit_rule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.yield
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupEvent
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.compareTo


class LimitRuleListScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinViewModel<LimitRuleViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::event

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedEvent = sharedViewModel::onEvent
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

        DisposableEffect(Unit) {
            event(LimitRuleEvent.SetList(sharedState.limitList))
            sharedEvent(PolicySharedEvent.RefreshSubscriptionLimit)

            onDispose {
                sharedEvent(PolicySharedEvent.SetLimitRule(state.value.limitRuleList))
            }
        }



        LimitRuleListUi(
            navigator = navigator,
            state = state.value,
            event = event,
            sharedState = sharedState
        )
    }
}

@Composable
fun LimitRuleListUi(
    navigator: Navigator?,
    state: LimitRuleState,
    event: (LimitRuleEvent) -> Unit,
    sharedState: PolicySharedState
) {


    var showLimitDialog by remember { mutableStateOf(false) }
    CustomDialog(
        showCloseButton = true,
        painter = painterResource(Res.drawable.dialog_subscription),
        title = stringResource(Res.string.limit_tugadi),
        message = stringResource(Res.string.sizda_foydalanish_chegarasini_qoshish),
        show = showLimitDialog,
        onDismiss = { showLimitDialog = false },
        buttonText = stringResource(Res.string.obuna_bolish),
        onButtonClick = {
            showLimitDialog = false
            navigator?.push(
                SubscriptionPaymentScreen(
                    selectedChild = sharedState.selectedChild
                )
            )
        }
    )

    LimitRuleDialog(
        show = state.showSetupDialog && sharedState.canUpdate,
        state = state,
        event = event,
        onDismiss = {
            event(LimitRuleEvent.ClearData)
            event(LimitRuleEvent.ShowSetupDialog(false))
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            modifier = Modifier
                .zIndex(1f),
            title = stringResource(Res.string.foydalanish_chegarasi),
            showBackButton = true,
            onBackClick = { navigator?.pop() }
        )



        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(ContainerPadding)
        ) {
            items(state.limitRuleList) {
                LimitRuleItem(
                    modifier = Modifier
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = {
                                event(
                                    LimitRuleEvent.SetUsageLimitData(
                                        it
                                    )
                                )
                                event(LimitRuleEvent.ShowSetupDialog(true))
                            }
                        ),
                    item = it,
                    onRemove = {
                        event(LimitRuleEvent.RemoveLimitRule(it))
                    },
                    canRemove = sharedState.canUpdate
                )
            }
        }

        if (sharedState.canUpdate) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomShadow(
                        shape = RoundedCornerShape(
                            topStart = ButtonCornerRadius,
                            topEnd = ButtonCornerRadius
                        ),
                        color = MaterialTheme.extendedColor.backgroundColor
                    )
                    .bottomShadow(
                        shape = RoundedCornerShape(
                            topStart = ButtonCornerRadius,
                            topEnd = ButtonCornerRadius
                        ),
                        color = MaterialTheme.extendedColor.backgroundColor,
                        lowerOffset = -5.dp,
                        radius = 10.dp

                    )
                    .background(MaterialTheme.extendedColor.backgroundColor)
                    .padding(
                        start = ContainerPadding,
                        end = ContainerPadding,
                        bottom = ContainerPadding
                    )


            )
            {
                CustomOutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        val count = sharedState.subscriptionLimit.limitRule
                        val listCount = state.limitRuleList.size

                        if (listCount >= count) {
                            showLimitDialog = true
                            return@CustomOutlinedButton
                        }
                        event(LimitRuleEvent.ShowSetupDialog(true))
                    },
                    text = stringResource(Res.string.oraliq_qoshish),
                    endingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.add_square),
                            contentDescription = "",
                        )
                    }

                )
                SpaceSmall()

                CustomButton(
                    text = stringResource(Res.string.saqlash),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ButtonHeight),
                    onClick = {
                        navigator?.popUntil {
                            it is PolicySetupScreen
                        }
                    }
                )
            }
        }
    }


}

@Preview
@Composable
private fun PreviewScheduleTimeListScreen() {
    LimitRuleListUi(
        navigator = null,
        state = LimitRuleState(),
        event = {},
        sharedState = PolicySharedState()
    )
}