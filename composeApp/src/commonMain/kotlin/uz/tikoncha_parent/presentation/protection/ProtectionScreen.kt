package uz.tikoncha_parent.presentation.protection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.dialog_warning
import tikoncha_parents.composeapp.generated.resources.farzand_chiqish_ruxsat
import tikoncha_parents.composeapp.generated.resources.farzand_ilovani_ochirmoqchi
import tikoncha_parents.composeapp.generated.resources.ha
import tikoncha_parents.composeapp.generated.resources.himoya
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqishni_tasdiqlaysizmi
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirishni_tasdiqlaysizmi
import tikoncha_parents.composeapp.generated.resources.ok
import tikoncha_parents.composeapp.generated.resources.qalqon_ochirishga_ruxsat
import tikoncha_parents.composeapp.generated.resources.qalqon_ochirish_ruxsat_desc
import tikoncha_parents.composeapp.generated.resources.xatolik
import tikoncha_parents.composeapp.generated.resources.yoq
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.protection.AccountRequestAction
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.Loading
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class ProtectionScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return

        val viewModel = koinScreenModel<ProtectionViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        // Tanlangan farzand — boshqa ekranlardagi kabi AppSettings'dan
        val selectedChild = AppSettings.selectedChild
        val childId = selectedChild?.userId ?: ""
        val childName = selectedChild?.name ?: ""

        LaunchedEffect(childId) {
            if (childId.isNotEmpty()) event(ProtectionEvent.LoadStatus(childId))
        }

        ProtectionUi(
            navigator = navigator,
            state = state,
            event = event,
            childId = childId,
            childName = childName,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtectionUi(
    navigator: Navigator?,
    state: ProtectionState,
    event: (ProtectionEvent) -> Unit,
    childId: String,
    childName: String
) {
    // ─────────── Dialog holatlari ───────────
    var approveStrictDialog by remember { mutableStateOf(false) }
    var allowLogoutDialog by remember { mutableStateOf(false) }
    var allowDeleteDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val loadErrorText = state.responseState.errorText()
    val actionErrorText = state.actionResponseState.errorText()

    LaunchedEffect(loadErrorText, actionErrorText) {
        if (loadErrorText.isNotEmpty() || actionErrorText.isNotEmpty()) {
            showErrorDialog = true
        }
    }

    Loading(state.responseState is ResponseState.Loading)

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = actionErrorText.ifEmpty { loadErrorText },
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showErrorDialog = false
            event(ProtectionEvent.ActionErrorDismissed)
        },
        onButtonClick = {
            showErrorDialog = false
            event(ProtectionEvent.ActionErrorDismissed)
        },
    )

    // Qalqon o'chirish so'rovini tasdiqlash
    CustomDialog(
        painter = painterResource(Res.drawable.dialog_warning),
        show = approveStrictDialog,
        title = stringResource(Res.string.qalqon_ochirishga_ruxsat),
        message = stringResource(Res.string.qalqon_ochirish_ruxsat_desc),
        buttonText = stringResource(Res.string.ha),
        buttonText2 = stringResource(Res.string.yoq),
        showCloseButton = true,
        onDismiss = { approveStrictDialog = false },
        onButtonClick = {
            approveStrictDialog = false
            state.strictDisableRequest?.id?.let {
                event(ProtectionEvent.ApproveStrictRequest(it))
            }
        },
    )

    // Hisobdan chiqishga ruxsat
    CustomDialog(
        painter = painterResource(Res.drawable.dialog_warning),
        show = allowLogoutDialog,
        title = stringResource(Res.string.hisobdan_chiqishni_tasdiqlaysizmi),
        message = stringResource(Res.string.farzand_chiqish_ruxsat),
        buttonText = stringResource(Res.string.ha),
        buttonText2 = stringResource(Res.string.yoq),
        showCloseButton = true,
        onDismiss = { allowLogoutDialog = false },
        onButtonClick = {
            allowLogoutDialog = false
            event(ProtectionEvent.AllowAccountRequest(AccountRequestAction.LOGOUT))
        },
    )

    // Ilovani o'chirishga ruxsat
    CustomDialog(
        painter = painterResource(Res.drawable.dialog_warning),
        show = allowDeleteDialog,
        title = stringResource(Res.string.ilovani_ochirishni_tasdiqlaysizmi),
        message = stringResource(Res.string.farzand_ilovani_ochirmoqchi),
        buttonText = stringResource(Res.string.ha),
        buttonText2 = stringResource(Res.string.yoq),
        showCloseButton = true,
        onDismiss = { allowDeleteDialog = false },
        onButtonClick = {
            allowDeleteDialog = false
            event(ProtectionEvent.AllowAccountRequest(AccountRequestAction.DELETE))
        },
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary),
    ) {
        CustomHeader(
            title = stringResource(Res.string.himoya),
            showBackButton = true,
            onBackClick = { navigator?.pop() },
            // trailingIcon = { ChildSelectionButton(...) }  // TODO: boshqa ekranlardagi kabi ulang
        )

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                if (childId.isNotEmpty()) event(ProtectionEvent.Refresh(childId))
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(ContainerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(key = "hero") {
                    ProtectionHeroCard(state = state, event = event)
                }

                if (state.showRequestsCard) {
                    item(key = "requests") {
                        ProtectionRequestsCard(
                            state = state,
                            childName = childName,
                            onApproveStrict = { approveStrictDialog = true },
                            onRejectStrict = { id ->
                                event(ProtectionEvent.RejectStrictRequest(id))
                            },
                            onAllowAccount = { action ->
                                when (action) {
                                    AccountRequestAction.LOGOUT -> allowLogoutDialog = true
                                    AccountRequestAction.DELETE -> allowDeleteDialog = true
                                    else -> {}
                                }
                            },
                            onDenyAccount = { action ->
                                event(ProtectionEvent.DenyAccountRequest(action))
                            },
                        )
                    }
                }

                if (state.missingRequiredPermissions.isNotEmpty()) {
                    item(key = "warning") {
                        MissingPermissionsBanner(missing = state.missingRequiredPermissions)
                    }
                }

                item(key = "permissions") {
                    ProtectionPermissionsCard(state = state)
                }
            }
        }
    }
}