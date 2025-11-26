package uz.tikoncha_parent.presentation.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.jadval
import tikoncha_parents.composeapp.generated.resources.jadval_nomini_kiriting
import tikoncha_parents.composeapp.generated.resources.limit_tugadi
import tikoncha_parents.composeapp.generated.resources.misol_o_quv_markaz
import tikoncha_parents.composeapp.generated.resources.shartlar_kiritish
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomDialogTextField
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebEvent
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebViewModel
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.extendedColor


@OptIn(InternalVoyagerApi::class)
class PolicyListScreen(
    val child: UserInfo?
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current?:return


        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        val sharedAppViewModel = navigator.koinNavigatorScreenModel<AppWebViewModel>()
        val sharedAppEvent = sharedAppViewModel::onEvent

        val viewModel = koinViewModel<PolicyViewModel>()
        val event = viewModel::onEvent
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit){
            event(PolicyEvent.SetSelectedChild(child))
            sharedAppEvent(AppWebEvent.ClearData)
            sharedEvent(PolicySharedEvent.ClearData)
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

    LoadingDialog(loading)
    var showErrorText by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(errorText){
        showErrorText = errorText.isNotEmpty()
    }

    CustomDialog(
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
//                sharedEvent(PolicySharedEvent.ClearData)
//                sharedAppEvent(AppWebEvent.ClearData)
                sharedEvent(PolicySharedEvent.SetPolicyTitle(finalTitle))
//                sharedEvent(PolicySharedEvent.SetSubscriptionLimit(state.subscriptionLimitEntity))
                showCreatePolicyDialog = false
                navigator?.push(PolicySetupScreen(state.selectedChild))
            }
        }
    )

    var showLimitDialog by remember { mutableStateOf(false) }


    CustomDialog(
        title = stringResource(Res.string.limit_tugadi),
        message = "Sizda yana boshqa jadval yaratish uchun limitingiz tugadi. Yana yangi jadval yaratish uchun PLUS obunasini sotib oling.",
        show = showLimitDialog,
//        lottieAsset = DialogLottie.WARNING,
        onDismiss = {showLimitDialog = false},
        onButtonClick = {
            showLimitDialog = false
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
//                        sharedEvent(PolicySharedEvent.ClearData)
//                        sharedAppEvent(AppWebEvent.ClearData)
//                        sharedEvent(PolicySharedEvent.SetSubscriptionLimit(state.subscriptionLimitEntity))
                        sharedEvent(PolicySharedEvent.SetPolicy(it))
                        navigator?.push(
                            PolicySetupScreen(
                                child = state.selectedChild,
                                policyItemUi = it
                            )
                        )
                    },
                    onEdit = {
//                        sharedEvent(PolicySharedEvent.ClearData)
//                        sharedAppEvent(AppWebEvent.ClearData)
                        sharedEvent(PolicySharedEvent.SetPolicy(it))
                        navigator?.push(
                            PolicySetupScreen(
                                child = state.selectedChild,
                                policyItemUi = it
                            )
                        )
                    }
                )
            }
        }

        CustomOutlinedButton(
            onClick = {
//                val count = state.subscriptionLimitEntity?.policy_count
//                val myPolicyCount = state.policyList.count { it.isMine }
//                if (count == null || count > myPolicyCount){
//                    showCreatePolicyDialog = true
//                }
//                else{
//                    showLimitDialog = true
//                }
                showCreatePolicyDialog = true
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