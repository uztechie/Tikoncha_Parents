package uz.tikoncha_parent.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.add_child.AddChildRegisterScreen
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.LogoHeader
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


class RegisterScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<RegisterViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        Register(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun Register(
    navigator: Navigator?,
    state: RegisterState,
    event: (RegisterEvent) -> Unit
) {

    val isOtpCodeValid = state.name != "" && state.fullName != "" && state.middleName != "" && state.idNumber != ""

    val registerLoading = state.registerResponseState is ResponseState.Loading
    val registerErrorText = state.registerResponseState.errorText()
    val registerSuccess = state.registerResponseState is ResponseState.Success

    LoadingDialog(registerLoading)
    var showRegisterErrorDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(registerErrorText) {
        if (registerErrorText.isNotEmpty()) {
            showRegisterErrorDialog = true
        }
    }
    CustomDialog(
        onDismiss = {showRegisterErrorDialog = false},
        show = showRegisterErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = registerErrorText,
        onButtonClick = {
            showRegisterErrorDialog = false
        }
    )

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            event.invoke(RegisterEvent.Reset)
           navigator?.replaceAll(AddChildRegisterScreen())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
            .padding(horizontal = 20.dp)
            .imePadding()
    ) {
        LogoHeader()
        CustomText(
            text = stringResource(Res.string.xush_kelibsiz),
            fontSize = LargeTextSize,
            fontWeight = FontWeight.W600,
        )
        SpaceMedium()
        CustomText(
            text = stringResource(Res.string.ro_yxatdan_o_tish_uchun_quyidagilarni_to_ldiring),
            fontSize = NormalTextSize,
            fontStyle = FontStyle.Normal,
            color = MaterialTheme.extendedColor.hintColor,
            fontWeight = FontWeight.W500,
        )

        SpaceMedium()

        CustomTextField(
            value = state.name,
            onValueChange = { event(RegisterEvent.OnNameInsert(it)) },
            modifier = Modifier.height(TextFieldHeight),
            label = stringResource(Res.string.ismingizni_kiriting),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.parent),
                    contentDescription = "Parent",
                    modifier = Modifier.size(NormalIconSize)
                )
            },
            fontWeight = FontWeight.W500
        )

        SpaceMedium()

        CustomTextField(
            value = state.fullName,
            onValueChange = { event(RegisterEvent.OnFullNameInsert(it)) },
            modifier = Modifier.height(TextFieldHeight),
            label = stringResource(Res.string.familiyangizni_kiriting),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.parent),
                    contentDescription = "Parent",
                    modifier = Modifier.size(NormalIconSize)
                )
            },
            fontWeight = FontWeight.W500
        )

        SpaceMedium()

        CustomTextField(
            value = state.middleName,
            onValueChange = { event(RegisterEvent.OnMiddleNameInsert(it)) },
            modifier = Modifier.height(TextFieldHeight),
            label = stringResource(Res.string.otangizni_ismini_kiriting),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.parent),
                    contentDescription = "Parent",
                    modifier = Modifier.size(NormalIconSize)
                )
            },
            fontWeight = FontWeight.W500
        )

        SpaceMedium()

        CustomTextField(
            value = state.idNumber,
            onValueChange = { event(RegisterEvent.OnIdNumberInsert(it)) },
            modifier = Modifier.height(TextFieldHeight),
            label = stringResource(Res.string.passport_id_raqamingiz),
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.id_card),
                    contentDescription = "Parent",
                    modifier = Modifier.size(NormalIconSize)
                )
            },
            fontWeight = FontWeight.W500
        )

        SpaceMedium()

        SegmentedToggle(
            options = listOf(
                stringResource(Res.string.ota) to painterResource(Res.drawable.father_icon),
                stringResource(Res.string.ona) to painterResource(Res.drawable.mather_icon),
            ),
            selectedIndex = state.genderIndex,
            onOptionSelected = {
                event(RegisterEvent.OnGenderSelected(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            fontWeight = FontWeight.W600,
            fontSize = NormalTextSize,
        )



        Spacer(modifier = Modifier.weight(1f))
        CustomButton(
            onClick = {
                event(RegisterEvent.OnConfirmClicked)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = isOtpCodeValid,
            text = stringResource(Res.string.keyingisi),
            fontWeight = FontWeight.W600,
            fontSize = NormalTextSize
        )
        SpaceLarge()
    }
}


@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        Register(
            navigator = null,
            state = RegisterState(),
            event = {}
        )
    }
}