package uz.tikoncha_parent.presentation.profile.user_edit

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.domain.model.GenderType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars


class UserEditScreen(
    private val userInfo: UserInfo
) : Screen {

    @Composable
    override fun Content() {
        
        val navigator = LocalNavigator.current

        val viewModel = koinScreenModel<UserInfoEditViewModel>{ parametersOf(userInfo) }
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(state.value.saveState) {
            if (state.value.saveState is ResponseState.Success) {
                navigator?.pop()
            }
        }

        UserEditUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun UserEditUi(
    navigator: Navigator?,
    state: UserEditState,
    event: (UserEditEvent) -> Unit
) {
    val isValid = (state.firstName.isNotBlank()
            && state.lastName.isNotBlank()
            && state.patronymic.isNotBlank())
    val selectedGenderIndex = when(state.genderType){
        GenderType.MALE -> 0
        GenderType.FEMALE -> 1
    }

    val loading = state.saveState is ResponseState.Loading
    val error = state.saveState.errorText()
    val showDialog = !error.isNullOrBlank()

    LoadingDialog(loading)

    CustomDialog(
        message = error,
        show = showDialog,
        title = stringResource(Res.string.xatolik),
        onDismiss = { event(UserEditEvent.ClearError) },
        onButtonClick = { event(UserEditEvent.ClearError) }
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary)
    ){
        CustomHeader(
            showBackButton = true,
            onBackClick = { navigator?.pop() },
            title = stringResource(Res.string.tahrirlash),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ContainerPadding)
                .imePadding()
        ) {
            SpaceMedium()
            CustomText(
                text = stringResource(Res.string.malumotlarni_tahrirlash),
                fontSize = NormalTextSize,
                fontStyle = FontStyle.Normal,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500,
            )

            SpaceMedium()

            CustomTextField(
                value = state.firstName,
                modifier = Modifier.height(TextFieldHeight),
                label = stringResource(Res.string.ismingizni_kiriting),
                onValueChange = { event(UserEditEvent.OnFirstName(it)) },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.parent),
                        contentDescription = "Parent",
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryColor),
                        modifier = Modifier.size(NormalIconSize)
                    )
                }
            )

            SpaceMedium()

            CustomTextField(
                value = state.lastName,
                modifier = Modifier.height(TextFieldHeight),
                onValueChange = { event(UserEditEvent.OnLastName(it)) },
                label = stringResource(Res.string.familiyangizni_kiriting),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.parent),
                        contentDescription = "Parent",
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryColor),
                        modifier = Modifier.size(NormalIconSize)
                    )
                }
            )

            SpaceMedium()

            CustomTextField(
                value = state.patronymic,
                modifier = Modifier.height(TextFieldHeight),
                onValueChange = { event(UserEditEvent.OnPatronymic(it)) },
                label = stringResource(Res.string.otangizni_ismini_kiriting),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.parent),
                        contentDescription = "Parent",
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryColor),
                        modifier = Modifier.size(NormalIconSize)
                    )
                }
            )
            SpaceMedium()

            SegmentedToggle(
                options = listOf(
                    stringResource(Res.string.ota) to painterResource(Res.drawable.father_icon),
                    stringResource(Res.string.ona) to painterResource(Res.drawable.mather_icon),
                ),
                selectedIndex = selectedGenderIndex,
                onOptionSelected = {index->
                    val gender = if (index == 0) GenderType.MALE else GenderType.FEMALE
                    event(UserEditEvent.OnGender(gender))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight)
            )

            Spacer(modifier = Modifier.weight(1f))
            CustomButton(
                onClick = {
                    event(UserEditEvent.OnSave)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = isValid,
                text = stringResource(Res.string.saqlash),
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        UserEditUi(
            navigator = null,
            state = UserEditState(),
            event = {}
        )
    }
}