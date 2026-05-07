package uz.tikoncha_parent.presentation.policy.policy_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.farzand_qo_shish
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.jadval_limit_tugadi_plus
import tikoncha_parents.composeapp.generated.resources.jadval_qoshish
import tikoncha_parents.composeapp.generated.resources.limit_tugadi
import tikoncha_parents.composeapp.generated.resources.sizning_cheklovlaringiz
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.platform.openUrl
import uz.tikoncha_parent.presentation.add_child.AddChildScreen
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.NoInternetDialog
import uz.tikoncha_parent.presentation.base.PermissionWarningCard
import uz.tikoncha_parent.presentation.base.SubscriptionBottomDialog
import uz.tikoncha_parent.presentation.base.rememberInternetCheck
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.presentation.new_home.SelectionChildBottonSheet
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


@OptIn(InternalVoyagerApi::class)
class PolicyListScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current?:return


        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent


        val viewModel = koinScreenModel<PolicyViewModel>()
        val event = viewModel::onEvent
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            event(PolicyEvent.RefreshPolicies)
            event(PolicyEvent.GetChildren)
        }

        BackHandler(true) {
            navigator.pop()
        }


        PolicyListUi(
            navigator = navigator,
            sharedEvent = sharedEvent,
            event = event,
            state = state,
            sharedState = sharedState,
        )
    }
}

@Composable
fun PolicyListUi(
    navigator: Navigator?,
    state: PolicyState,
    sharedState: PolicySharedState,
    event: (PolicyEvent) -> Unit = {},
    sharedEvent: (PolicySharedEvent) -> Unit = {},
) {
    val errorText = state.policyResponseState.errorText()
    var showErrorText by remember { mutableStateOf(false) }
    val loading = state.policyResponseState is ResponseState.Loading
    var showPolicyLimitDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    val internetCheck = rememberInternetCheck(refreshScope)
    var showDialog by remember { mutableStateOf(false) }

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.surface
    )


    LoadingDialog(loading)

    NoInternetDialog(internetCheck)


    LaunchedEffect(errorText) { showErrorText = errorText.isNotEmpty() }

    if (showDialog) {
        SelectionChildBottonSheet(
            navigator = navigator,
            items = state.childrenList,
            onDismiss = { showDialog = false },
            selectedItem = state.selectedChild,
            title = stringResource(Res.string.farzandlaringiz),
            onItemSelected = {
                event(PolicyEvent.OnChildSelected(it))
                showDialog = false
            }
        )
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        title = stringResource(Res.string.xatolik),
        message = errorText,
        show = showErrorText,
        onDismiss = {
            showErrorText = false
        },
        onButtonClick = {
            showErrorText = false
        }
    )

    SubscriptionBottomDialog(
        show = showPolicyLimitDialog,
        title = stringResource(Res.string.limit_tugadi),
        message = stringResource(Res.string.jadval_limit_tugadi_plus),
        onConfirm = {
            showPolicyLimitDialog = false
            navigator?.push(SubscriptionPaymentScreen())
        },
        onDismiss = {
            showPolicyLimitDialog = false
        }
    )

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            internetCheck.check {
                isRefreshing = true
                event(PolicyEvent.RefreshPolicies)
                delay(500)
                isRefreshing = false
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(systemBars.modifier)
                .background(AppColors.bg.secondary)
        ) {

            CustomHeader(
                title = stringResource(Res.string.sizning_cheklovlaringiz),
                showBackButton = true,
                onBackClick = {
                    navigator?.pop()
                },
                modifier = Modifier.fillMaxWidth()
            )

            ChildSelectionButton(
                text = state.selectedChild?.name ?: "",
                imageUrl = state.selectedChild?.avatarUrl ?: "",
                label = stringResource(Res.string.farzand_qo_shish),
                trailingIcon = state.childrenList.isNotEmpty(),
                onClick = {
                    if (state.childrenList.isEmpty()) {
                        navigator?.push(AddChildScreen())
                    } else {
                        showDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(horizontal = 5.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {

                    if (state.permissionIssueList.isNotEmpty()) {
                        state.permissionIssueList.forEach {
                            PermissionWarningCard(
                                title = it.title,
                                body = it.body,
                                videoUrl = it.video_url,
                                onVideoClick = { url ->
                                    openUrl(url)
                                }
                            )
                            Space(12.dp)
                        }
                    }

                    if (state.policies.isEmpty() && !loading) {
                        CreatePolicyCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .simpleShadow(),
                            onClick = {
                                if (state.canCreatePolicy) {
                                    sharedEvent(PolicySharedEvent.ClearData)
                                    sharedEvent(PolicySharedEvent.SetSubscriptionLimit(state.subscriptionLimit))
                                    state.selectedChild?.let { child ->
                                        sharedEvent(
                                            PolicySharedEvent.SetSelectedChild(child)
                                        )
                                    }
                                    navigator?.push(PolicySetupScreen())
                                } else {
                                    showPolicyLimitDialog = true
                                }
                            }
                        )
                    }
                }

                items(
                    items = state.policies,
                    key = { it.ruleId }
                ) {
                    PolicyListItem(
                        modifier = Modifier,
                        policy = it,
                        onClick = {
                            sharedEvent(PolicySharedEvent.ClearData)
                            state.selectedChild?.let { child ->
                                sharedEvent(PolicySharedEvent.SetSelectedChild(child))
                            }
                            sharedEvent(PolicySharedEvent.SetSubscriptionLimit(state.subscriptionLimit))
                            sharedEvent(PolicySharedEvent.SetPolicy(it))
                            navigator?.push(PolicySetupScreen())
                        }
                    )
                }

//                item {
//                    Space(16.dp)
//                    Text(
//                        text = stringResource(Res.string.shablonlar),
//                        color = AppColors.text.primary,
//                        style = AppTypography.titleLgSemiBold
//                    )
//                    Space(12.dp)
//                    PolicyTemplateEmptyItem(
//                        icon = painterResource(Res.drawable.time_large_icon),
//                        title = stringResource(Res.string.uyqu_vaqti_rejasi),
//                        desc = stringResource(Res.string.farzandingiz_kun_davomida_telefondan_qancha),
//                        onClick = {
//                            if (state.canCreatePolicy) {
//                                sharedEvent(PolicySharedEvent.ClearData)
//                                sharedEvent(PolicySharedEvent.SetSubscriptionLimit(state.subscriptionLimit))
//                                state.selectedChild?.let { child ->
//                                    sharedEvent(PolicySharedEvent.SetSelectedChild(child))
//                                    sharedEvent(PolicySharedEvent.SetPolicyAction(PolicyAction.ALLOW))
//                                }
//                                navigator?.push(SleepTemplateSetupScreen())
//                            } else {
//                                showPolicyLimitDialog = true
//                            }
//                        }
//                    )
//                    Space(8.dp)
//                    PolicyTemplateEmptyItem(
//                        icon = painterResource(Res.drawable.timer_policy),
//                        title = stringResource(Res.string.ilova_taymeri),
//                        desc = stringResource(Res.string.ilovalarni_tanlang_va_ular_uchun_umumiy),
//                        onClick = {}
//                    )
//                    Space(8.dp)
//                    PolicyTemplateEmptyItem(
//                        icon = painterResource(Res.drawable.internet),
//                        title = stringResource(Res.string.kontent_cheklovlari),
//                        desc = stringResource(Res.string.farzandingizni_nomaqbul_kontentdan_himoya_qiling),
//                        onClick = {}
//                    )
//                }
            }

            if (state.policies.isNotEmpty()) {
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
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.jadval_qoshish),
                        onClick = {
                            if (state.canCreatePolicy) {
                                sharedEvent(PolicySharedEvent.ClearData)
                                state.selectedChild?.let { child ->
                                    sharedEvent(PolicySharedEvent.SetSelectedChild(child))
                                }
                                sharedEvent(PolicySharedEvent.SetSubscriptionLimit(state.subscriptionLimit))
                                navigator?.push(PolicySetupScreen())
                            } else {
                                showPolicyLimitDialog = true
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        PolicyListUi(
            navigator = null,
            state = PolicyState(),
            sharedState = PolicySharedState(),
            event = {},
        )
    }
}