package org.example.project.presentation.register

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
import org.example.project.presentation.add_child.AddChildScreen
import org.example.project.presentation.base.CustomDialog
import org.example.project.presentation.base.CustomTextField
import org.example.project.presentation.base.LoadingDialog
import org.example.project.presentation.base.LogoHeader
import org.example.project.presentation.base.SegmentedToggle
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.create_password.CreatePasswordScreen


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

    LoadingDialog(state.registerLoading)
    var showRegisterErrorDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(state.registerError) {
        if (state.registerError.isNotEmpty()) {
            showRegisterErrorDialog = true
        }
    }
    CustomDialog(
        onDismiss = {showRegisterErrorDialog = false},
        show = showRegisterErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = state.registerError,
        onButtonClick = {
            showRegisterErrorDialog = false
        }
    )

    LaunchedEffect(state.registerSuccess) {
        if (state.registerSuccess) {
            event.invoke(RegisterEvent.Reset)
           navigator?.replaceAll(AddChildScreen())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.W500,
        )

        SpaceMedium()

        CustomTextField(
            value = state.name,
            onValueChange = { event(RegisterEvent.OnNameInsert(it)) },
            hasBorder = true,
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
            hasBorder = true,
            modifier = Modifier.height(TextFieldHeight),
            label = stringResource(Res.string.familiyangizni_kiriting),
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
            value = state.middleName,
            onValueChange = { event(RegisterEvent.OnMiddleNameInsert(it)) },
            hasBorder = true,
            modifier = Modifier.height(TextFieldHeight),
            label = stringResource(Res.string.otangizni_ismini_kiriting),
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
            value = state.idNumber,
            onValueChange = { event(RegisterEvent.OnIdNumberInsert(it)) },
            hasBorder = true,
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
    Register(
        navigator = null,
        state = RegisterState(),
        event = {}
    )
}