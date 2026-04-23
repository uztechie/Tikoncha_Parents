@file:Suppress("DEPRECATION")

package uz.tikoncha_parent.presentation.add_child

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.common.Util.format6DigitCode
import uz.tikoncha_parent.platform.copyPlainText
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.profile.children.ChildrenScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class AddChildScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<ChildViewmodel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        AddChildUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}


@Composable
fun AddChildUi(
    navigator: Navigator?,
    state: ChildState,
    event: (ChildEvent) -> Unit
) {
    var showErrorDialog by remember { mutableStateOf(false) }

    val isLoading = state.responseState is ResponseState.Loading
    val errorText = state.responseState.errorText()
    val isSuccess = state.responseState is ResponseState.Success

    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val formatted = remember(state.confirmCode) { format6DigitCode(state.confirmCode) }
    val canEditePhone by remember(state.confirmCode) { mutableStateOf(state.confirmCode.isEmpty()) }

    LoadingDialog(isLoading)
    LaunchedEffect(errorText) {
        showErrorDialog = errorText.isNotEmpty()
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = state.responseState.errorText(),
        buttonText = stringResource(Res.string.ok),
        onDismiss = {
            showErrorDialog = false
        },
        onButtonClick = {
            showErrorDialog = false
        }
    )

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_success),
        show = state.childJoined,
        title = stringResource(Res.string.muvaffaqiyatli),
        buttonText = stringResource(Res.string.ok),
        message = stringResource(Res.string.farzand_ulan_di),
        onDismiss = {
            event(ChildEvent.OnSuccessDismissed)
        },
        onButtonClick = {
            val addedPhone = state.fullNumber
            event(ChildEvent.OnSuccessDismissed)
            navigator?.replace(ChildrenScreen(highlightPhone = addedPhone))
        }
    )

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            event(ChildEvent.Reset)
//            navigator?.push(ChildConfirmCodeScreen(confirmCode = state.confirmCode))
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        if (state.confirmCode.isNotEmpty() && !state.childJoined) {
            event(ChildEvent.StartWatching)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        event(ChildEvent.StopWatching)
    }

    LaunchedEffect(state.confirmCode) {
        if (state.confirmCode.isNotEmpty() && !state.childJoined) {
            event(ChildEvent.StartWatching)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            event(ChildEvent.StopWatching)
        }
    }

    var enableButton by remember { mutableStateOf(false) }
    LaunchedEffect(state.number) {
        enableButton = state.number.length >= 9
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.farzand_qoshish),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SpaceLarge()

            CustomText(
                text = stringResource(Res.string.farzandlaringiz),
                fontSize = 16.sp,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500
            )
            SpaceMedium()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        width = 1.dp,
                        color = if (state.accept) PrimaryColor else Color.Transparent,
                        shape = RoundedCornerShape(TextFieldCornerRadius)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.extendedColor.cardColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingCornerRadius)
                ) {
                    CustomText(
                        text = stringResource(Res.string.farzandingiz_telefon_raqamini_kiriting),
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W500
                    )
                    SpaceMedium()

                    ChildPhoneInputField(
                        isAccepted = state.accept,
                        phoneNumber = state.number,
                        onPhoneNumberChange = { newNumber ->
                            if (canEditePhone) {
                                event(ChildEvent.OnNumberInsert(newNumber))
                            }
                        }
                    )
                }
            }
            SpaceLarge()
            SpaceLarge()

            if (state.confirmCode.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.extendedColor.cardColor,
                            RoundedCornerShape(TextFieldCornerRadius)
                        )
                        .height(TextFieldHeight),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.password_check),
                        contentDescription = null,
                        tint = MaterialTheme.extendedColor.hintColor,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                scope.launch {
                                    copyPlainText(clipboard, formatted)
                                }
                            }
                    )
                    SpaceSmall()

                    CustomText(
                        text = formatted,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                }
                SpaceSmall()

                CustomText(
                    text = stringResource(Res.string.confirm_code_instruction),
                    fontSize = NormalTextSize,
                    color = MaterialTheme.extendedColor.hintColor,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            if (canEditePhone) {
                CustomButton(
                    onClick = {
                        event(ChildEvent.OnAddClicked)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .height(ButtonHeight),
                    enabled = enableButton,
                    text = stringResource(Res.string.qoshish)
                )
                SpaceLarge()
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        AddChildUi(
            navigator = null,
            state = ChildState(),
            event = {}
        )
    }
}