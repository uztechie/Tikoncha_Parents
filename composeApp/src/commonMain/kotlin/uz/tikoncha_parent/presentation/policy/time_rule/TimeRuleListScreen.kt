package uz.tikoncha_parent.presentation.policy.time_rule

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.dialog_subscription
import tikoncha_parents.composeapp.generated.resources.faol_vaqt
import tikoncha_parents.composeapp.generated.resources.limit_tugadi
import tikoncha_parents.composeapp.generated.resources.obuna_bolish
import tikoncha_parents.composeapp.generated.resources.oraliq_qoshish
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.sizda_vaqt_oraligi_qoshish
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.bottomShadow
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class TimeRuleListScreen(): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val vieModel = koinScreenModel<TimeRuleViewModel>()
        val state by vieModel.state.collectAsStateWithLifecycle()
        val event = vieModel::event



        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedEvent = sharedViewModel::onEvent
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

        DisposableEffect(Unit) {
            event(TimeRuleEvent.SetList(sharedState.timeList))
//            sharedEvent(PolicySharedEvent.RefreshSubscriptionLimit)
            onDispose {
                sharedEvent(PolicySharedEvent.SetTimeRule(state.timeList))
            }
        }


        TimeRuleListUi(
            state = state,
            event = event,
            sharedState = sharedState,
            navigator = navigator
        )


    }

}


@Composable
fun TimeRuleListUi(
    navigator: Navigator?,
    state: TimeRuleState,
    event: (TimeRuleEvent) -> Unit,
    sharedState: PolicySharedState
)
{

    var showLimitDialog by remember { mutableStateOf(false) }
    CustomDialog(
        showCloseButton = true,
        buttonText = stringResource(Res.string.obuna_bolish),
        painter = painterResource(Res.drawable.dialog_subscription),
        title = stringResource(Res.string.limit_tugadi),
        message = stringResource(Res.string.sizda_vaqt_oraligi_qoshish),
        show = showLimitDialog,
        onDismiss = {showLimitDialog = false},
        onButtonClick = {
            showLimitDialog = false
//            navigator?.push(
//                SubscriptionPaymentScreen(
//                    selectedChild = sharedState.selectedChild
//                )
//            )
        }
    )

    LaunchedEffect(Unit) {
        event(TimeRuleEvent.BeginCreateRule())
    }





    TimeRuleDialog(
        show = state.showSetupDialog && sharedState.canUpdate,
        state = state,
        event = event,
        onDismiss = {
            event(TimeRuleEvent.ClearTime)
            event(TimeRuleEvent.ShowSetupDialog(false))

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
            title = stringResource(Res.string.faol_vaqt),
            showBackButton = true,
            onBackClick = { navigator?.pop() }
        )



        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(
                ContainerPadding
            )
        ) {
            items(state.timeList) {
                TimeRuleItem(
                    modifier = Modifier
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = {
                                event(TimeRuleEvent.OpenEdite(it))
                            }
                        ),
                    item = it,
                    onRemove = {
                        event(TimeRuleEvent.RemoveTimeRule(it))
                    },
                    canRemove = sharedState.canUpdate
                )
            }
        }

        if (sharedState.canUpdate){
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
                    enabled = !state.timeList.any { it.allDay && it.weekDays.size == 7 },
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        val count = 0
                        val listCount = state.timeList.size

                        if (listCount >= count) {
                            showLimitDialog = true
                            return@CustomOutlinedButton
                        }
                        event(TimeRuleEvent.OpenCreate)
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
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        TimeRuleListUi(
            navigator = null,
            state = TimeRuleState(),
            event = {},
            sharedState = PolicySharedState()
        )
    }
}





