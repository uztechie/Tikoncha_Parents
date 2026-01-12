package uz.tikoncha_parent.presentation.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import kotlinx.coroutines.yield
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.dialog_subscription
import tikoncha_parents.composeapp.generated.resources.farzandingizni_tanlang
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.jadval
import tikoncha_parents.composeapp.generated.resources.jadval_nomini_kiriting
import tikoncha_parents.composeapp.generated.resources.misol_o_quv_markaz
import tikoncha_parents.composeapp.generated.resources.obuna
import tikoncha_parents.composeapp.generated.resources.obuna_bolish
import tikoncha_parents.composeapp.generated.resources.shartlar_kiritish
import tikoncha_parents.composeapp.generated.resources.obuna_dialog_message
import tikoncha_parents.composeapp.generated.resources.sotib_olish
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.common.ScreenJson
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomDialogTextField
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebEvent
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebViewModel
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.extendedColor


@OptIn(InternalVoyagerApi::class)
class PolicyListScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current?:return


        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        val sharedAppViewModel = navigator.koinNavigatorScreenModel<AppWebViewModel>()
        val sharedAppEvent = sharedAppViewModel::onEvent

        val viewModel = koinScreenModel<PolicyViewModel>()
        val event = viewModel::onEvent
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit){
            event(PolicyEvent.GetPolicies)
        }

        LaunchedEffect(state.selectedChild){
            yield()
            sharedAppEvent(AppWebEvent.ClearData)
            sharedEvent(PolicySharedEvent.ClearData)
            state.selectedChild?.let { sharedEvent(PolicySharedEvent.SetSelectedChild(it)) }
        }

        BackHandler(true){
            sharedAppEvent(AppWebEvent.ClearAppList)
            navigator.pop()
        }


        PolicyListUi(
            navigator = navigator,
            sharedEvent = sharedEvent,
            event = event,
            state = state,
            sharedState = sharedState,
            sharedAppEvent = sharedAppEvent
        )
    }
}

@Composable
fun PolicyListUi(
    navigator: Navigator?,
    state: PolicyState,
    event: (PolicyEvent) -> Unit = {},
    sharedEvent: (PolicySharedEvent) -> Unit = {},
    sharedState: PolicySharedState,
    sharedAppEvent: (AppWebEvent) -> Unit
){


    val loading = state.policyResponseState is ResponseState.Loading
    val errorText = state.policyResponseState.errorText()



    var showChildrenDialog by remember { mutableStateOf(false) }

    LoadingDialog(loading)
    var showErrorText by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(errorText){
        showErrorText = errorText.isNotEmpty()
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

    CustomListDialog(
        title = stringResource(Res.string.farzandlaringiz),
        items = state.childrenList,
        show = showChildrenDialog,
        loading = false,
        errorMessage = "",
        onItemSelected = {
            event(PolicyEvent.SetSelectedChild(it))
            sharedEvent(PolicySharedEvent.SetSelectedChild(it))
        },
        onDismiss = {
            showChildrenDialog = false
        }
    )




    var showCreatePolicyDialog by remember { mutableStateOf(false) }
    var title by rememberSaveable(sharedState.policyTitle) { mutableStateOf(sharedState.policyTitle) }

    CustomDialogTextField(
        enabled = if (title.length < 2) false else true,
        show = showCreatePolicyDialog,
        title = stringResource(Res.string.jadval_nomini_kiriting),
        label = stringResource(Res.string.misol_o_quv_markaz),
        value = title,
        onValueChange = {
            title = it
        },
        onDismiss = {showCreatePolicyDialog = false},
        onButtonClick = {
            val finalTitle = title.trim()
            if (finalTitle.isNotBlank()) {
                sharedEvent(PolicySharedEvent.SetPolicyTitle(finalTitle))
                showCreatePolicyDialog = false
                val selectedChild = ScreenJson.encode(state.selectedChild)
                navigator?.push(PolicySetupScreen(selectedChild))
            }
        }
    )

    var showLimitDialog by remember { mutableStateOf(false) }


    CustomDialog(
        showCloseButton = true,
        painter = painterResource(Res.drawable.dialog_subscription),
        title = stringResource(Res.string.obuna_bolish),
        message = stringResource(Res.string.obuna_dialog_message),
        show = showLimitDialog,
        buttonText = stringResource(Res.string.obuna_bolish),
        onDismiss = {showLimitDialog = false},
        onButtonClick = {
            showLimitDialog = false
            navigator?.push(
                SubscriptionPaymentScreen(
                    selectedChild = state.selectedChild
                )
            )
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        var title = stringResource(Res.string.jadval)

        CustomHeader(
            title = title,
            showBackButton = true,
            onBackClick = {
                sharedAppEvent(AppWebEvent.ClearAppList)
                navigator?.pop()
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                ChildSelectionButton(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 10.dp),
                    text = state.selectedChild?.name?:"",
                    imageUrl = state.selectedChild?.avatarUrl?:"",
                    label = stringResource(Res.string.farzandingizni_tanlang),
                    onClick = {
                        showChildrenDialog = true
                    },
                )
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(ContainerPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(state.policies){
                PolicyListItem(
                    modifier = Modifier,
                    policy = it,
                    onClick = {
                        sharedEvent(PolicySharedEvent.SetPolicy(it))
                        val child = ScreenJson.encode(state.selectedChild)
                        val policyItemUi = ScreenJson.encode(it)
                        navigator?.push(
                            PolicySetupScreen(
                                childJson = child,
                                policyItemUiJson = policyItemUi
                            )
                        )
                    },
                    onEdit = {
                        sharedEvent(PolicySharedEvent.SetPolicy(it))
                        val child = ScreenJson.encode(state.selectedChild)
                        val policyItemUi = ScreenJson.encode(it)
                        navigator?.push(
                            PolicySetupScreen(
                                childJson = child,
                                policyItemUiJson = policyItemUi
                            )
                        )
                    }
                )
            }
        }

        CustomOutlinedButton(
            onClick = {
                val count = sharedState.subscriptionLimit.policyCount
                Logger.d("PolicyList", "subscriptionLimit=${sharedState.subscriptionLimit}")
                val myPolicyCount = state.policies.count { it.isMine }
                if (count > myPolicyCount){
                    showCreatePolicyDialog = true
                }
                else{
                    showLimitDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ContainerPadding),
            text = stringResource(Res.string.shartlar_kiritish),
            endingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.add_square),
                    contentDescription = "",
                    tint = PrimaryColor
                )
            }
        )
        SpaceSmall()
    }


}