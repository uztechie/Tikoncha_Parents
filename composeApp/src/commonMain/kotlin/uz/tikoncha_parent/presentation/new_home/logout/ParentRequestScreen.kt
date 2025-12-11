package uz.tikoncha_parent.presentation.new_home.logout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.farzand_chiqish_ruxsat
import tikoncha_parents.composeapp.generated.resources.farzand_ilovani_ochirmoqchi
import tikoncha_parents.composeapp.generated.resources.ha
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqishni_bekor_qilasizmi
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqishni_bekor_qilish
import tikoncha_parents.composeapp.generated.resources.hisobdan_chiqishni_tasdiqlaysizmi
import tikoncha_parents.composeapp.generated.resources.ilovani_ochirishni_tasdiqlaysizmi
import tikoncha_parents.composeapp.generated.resources.ok
import tikoncha_parents.composeapp.generated.resources.sorovlar
import tikoncha_parents.composeapp.generated.resources.tasdiqlash
import tikoncha_parents.composeapp.generated.resources.xatolik
import tikoncha_parents.composeapp.generated.resources.yoq
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class ParentRequestScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current?:return

        val viewModel = koinViewModel<ParentRequestViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(Unit){
            viewModel.loadParentRequests()
        }

        LogoutUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}
@Composable
fun LogoutUi(
    navigator: Navigator?,
    state: ParentRequestState,
    event: (ParentRequestEvent) -> Unit
) {

    var logoutDialog by remember { mutableStateOf(false) }
    var canselDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    val loading =
        state.listResponseState is ResponseState.Loading || state.createResponseState is ResponseState.Loading || state.deleteResponseState is ResponseState.Loading

    val listErrorText = state.listResponseState.errorText()
    val listSuccess = state.listResponseState is ResponseState.Success

    val createErrorText = state.createResponseState.errorText()
    val createSuccess = state.createResponseState is ResponseState.Success

    val deleteErrorText = state.deleteResponseState.errorText()
    val deleteSuccess = state.deleteResponseState is ResponseState.Success


    var showErrorDialog by remember() {
        mutableStateOf(false)
    }
    var showSuccessDialog by remember() {
        mutableStateOf(false)
    }

    LaunchedEffect(createErrorText, listErrorText, deleteErrorText) {
        if (createErrorText.isNotEmpty() || listErrorText.isNotEmpty() || deleteErrorText.isNotEmpty()) {
            showErrorDialog = true
        }
    }
    LaunchedEffect(createSuccess, deleteSuccess) {
        if (createSuccess  || deleteSuccess) {
            showSuccessDialog = true
        }
    }

    CustomDialog(
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = createErrorText.ifEmpty { listErrorText }.ifEmpty { deleteErrorText },
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showErrorDialog = false
            event(ParentRequestEvent.ResetResponseState)
        },
        onButtonClick = {
            showErrorDialog = false
            event(ParentRequestEvent.ResetResponseState)
        }
    )

    CustomDialog(
        title = stringResource(Res.string.hisobdan_chiqishni_tasdiqlaysizmi),
        message = stringResource(Res.string.farzand_chiqish_ruxsat),
        buttonText = stringResource(Res.string.ha),
        buttonText2 = stringResource(Res.string.yoq),
        showCloseButton = true,
        isRow = true,
        show = logoutDialog,
        onDismiss = {
            logoutDialog = false
        },
        onButtonClick = {
            event(ParentRequestEvent.CreateRequest)
            logoutDialog = false

        }
    )

    CustomDialog(
        title = stringResource(Res.string.ilovani_ochirishni_tasdiqlaysizmi),
        message = stringResource(Res.string.farzand_ilovani_ochirmoqchi),
        buttonText = stringResource(Res.string.ha),
        buttonText2 = stringResource(Res.string.yoq),
        showCloseButton = true,
        isRow = true,
        show = deleteDialog,
        onDismiss = {
            deleteDialog = false
        },
        onButtonClick = {
            event(ParentRequestEvent.CreateRequest)
            deleteDialog = false
        }
    )

    CustomDialog(
        title = stringResource(Res.string.hisobdan_chiqishni_bekor_qilish),
        message = stringResource(Res.string.hisobdan_chiqishni_bekor_qilasizmi),
        buttonText = stringResource(Res.string.ha),
        buttonText2 = stringResource(Res.string.yoq),
        showCloseButton = true,
        isRow = true,
        show = canselDialog,
        onDismiss = {
            canselDialog = false
        },
        onButtonClick = {
            event(ParentRequestEvent.CreateRequest)
            canselDialog = false
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ){
        CustomHeader(
            title = stringResource(Res.string.sorovlar),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(ContainerPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.items) { item ->
                ParentRequestItem(
                    parentRequestUi = item,
                    onActionClick = {
                        when(item.type){
                            ParentRequestType.LOGOUT -> {
                                logoutDialog = true
                            }
                            ParentRequestType.DELETE -> {
                                deleteDialog = true
                            }
                        }
                        event(ParentRequestEvent.SetType(item.type))
                    },
                    onCancelRequestClick = {
                        canselDialog = true
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
        LogoutUi(
            navigator = null,
            state = ParentRequestState(),
            event = {}
        )
    }
}