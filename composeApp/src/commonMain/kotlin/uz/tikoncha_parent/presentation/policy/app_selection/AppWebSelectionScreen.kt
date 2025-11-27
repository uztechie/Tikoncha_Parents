package uz.tikoncha_parent.presentation.policy.app_selection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButtonDefaults.LargeIconSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ilovalar
import tikoncha_parents.composeapp.generated.resources.jadval
import tikoncha_parents.composeapp.generated.resources.limit_tugadi
import tikoncha_parents.composeapp.generated.resources.saqlash
import tikoncha_parents.composeapp.generated.resources.veb_sayt
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor




class AppWebSelectionScreen(): Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return

        val viewModel = navigator.koinNavigatorScreenModel<AppWebViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        AppWebSelectionUi(
            state = state,
            event = event,
            sharedState = sharedState
        )

        Logger.d("SALOM", "selectedChild=${sharedState.selectedChild?.userId}")

        LaunchedEffect(Unit){
            event(AppWebEvent.GetAppsFromServer)
            event(AppWebEvent.RefreshSubscriptionLimit)
        }
    }

}

@Composable
fun AppWebSelectionUi(
    state: AppWebState,
    event: (AppWebEvent) -> Unit,
    sharedState: PolicySharedState

) {

    val navigator = LocalNavigator.current




    CustomDialog(
        title = stringResource(Res.string.limit_tugadi),
        message = "Sizda ${state.subscriptionLimit.appCount } dan ko'p ilovalarni tanlay olmaysiz. Ko'proq ilovalarni qo'shish uchun PLUS obunani sotib oling.",
        show = state.showLimitReachedDialog,
//        lottieAsset = DialogLottie.WARNING,
        onDismiss = {
            event(AppWebEvent.DismissLimitDialog)
        },
        onButtonClick = {
            event(AppWebEvent.DismissLimitDialog)
        }
    )


    val loading = state.appsResponseState is ResponseState.Loading
    val errorText = state.appsResponseState.errorText()

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





    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            title = stringResource(Res.string.jadval)
        )

        SpaceMedium()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            SegmentedToggle(
                options = listOf(
                    stringResource(Res.string.ilovalar) to null,
                    stringResource(Res.string.veb_sayt) to null,
                ),
                selectedIndex = state.appWebSelectionIndex,
                onOptionSelected = {
                    event(AppWebEvent.OnAppWebSelected(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = NormalTextSize,
            )

            SpaceLarge()

            LazyColumn(
                contentPadding = PaddingValues(vertical = ContainerPadding),
                modifier = Modifier
//                    .bottomShadow(
//                        shape = RoundedCornerShape(
//                            topStart = ButtonCornerRadius,
//                            topEnd = ButtonCornerRadius
//                        ),
//                        color = MaterialTheme.extendedColor.backgroundColor
//                    )
//                    .bottomShadow(
//                        shape = RoundedCornerShape(
//                            topStart = ButtonCornerRadius,
//                            topEnd = ButtonCornerRadius
//                        ),
//                        color = MaterialTheme.extendedColor.backgroundColor,
//                        lowerOffset = -5.dp,
//                        radius = 10.dp
//
//                    )
                    .weight(1f)
            ) {
                when(state.appWebSelectionIndex){
                    0 -> {
                        items(state.apps) { app ->
                            AppRowItem(
                                enabled = sharedState.canUpdate,
                                modifier = Modifier
                                    .padding(vertical = 10.dp),
                                app = app,
                                onCheckedChange = {
                                    event(
                                        AppWebEvent.ToggleApp(
                                            app = app,
                                            checked = it
                                        )
                                    )
                                },
                            )
                            DividerHorizontal(
                                modifier = Modifier
                                    .padding(start = LargeIconSize+15.dp)
                            )
                        }
                    }

                    1 -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
//                                LottiePlayer(
//                                    size = 300.dp,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(ContainerPadding),
//                                    filePath = "json/coming_soon.json",
//                                    iterations = 1,
//                                    speed = 0.5f
//                                )
                            }
                        }

                    }
                }

            }



            if (sharedState.canUpdate){
                CustomButton(
                    text = stringResource(Res.string.saqlash),
                    onClick = {
                        navigator?.pop()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                SpaceMedium()
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
        AppWebSelectionUi(
            state = AppWebState(),
            event = {},
            sharedState = PolicySharedState()
        )
    }
}