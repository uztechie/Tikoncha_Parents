@file:Suppress("DEPRECATION")

package uz.tikoncha_parent.presentation.add_child

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.child_confirm_cod.ChildConfirmCodeScreen
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

class AddChildScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<ChildViewmodel>()
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


    var showDialog by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(state.errorMessage) {
        showDialog = !state.errorMessage.isNullOrEmpty()
    }

    LoadingDialog(show = state.loading)
    CustomDialog(
        show = showDialog,
        title = stringResource(Res.string.xatolik),
        message = state.errorMessage?:"",
        buttonText = stringResource(Res.string.ok),
        onDismiss = {
            showDialog = false
        },
        onButtonClick = {
            showDialog = false
        }
    )

    LaunchedEffect(state.success) {
        if (state.success){
            event(ChildEvent.Reset)
            navigator?.push(ChildConfirmCodeScreen(confirmCode = state.confirmCode))
        }
    }



    var enableButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.number){
        enableButton = state.number.length>=9
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
                    .border(width = 1.dp, color = if (state.accept) PrimaryColor else MaterialTheme.extendedColor.borderColor, shape = RoundedCornerShape(TextFieldCornerRadius)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.extendedColor.cardColor)
            )
            {
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
                        phoneNumber = state.number,
                        onPhoneNumberChange = { newNumber ->
                          event(ChildEvent.OnNumberInsert(newNumber))
                        },
                        isAccepted = state.accept
                    )
                }
            }


            SpaceLarge()

            Spacer(modifier = Modifier.weight(1f))


            CustomButton(
                onClick = {
                    event(ChildEvent.OnAddClicked)
                    event(ChildEvent.Clear)
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = enableButton,
                text = stringResource(Res.string.qoshish),
                fontWeight = FontWeight.W500,
                fontSize = NormalTextSize
            )
            SpaceLarge()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    AddChildUi(
        navigator = null,
        state = ChildState(),
        event = {}
    )
}