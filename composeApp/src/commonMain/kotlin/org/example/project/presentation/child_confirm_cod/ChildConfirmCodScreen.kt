package org.example.project.presentation.child_confirm_cod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.LogoHeader
import org.example.project.presentation.base.theme.*
import org.example.project.presentation.base.theme.SpaceLarge
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.main.MainScreen

class ChildConfirmCodScreen(): Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<ChildConfirmViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        ChildCodUI(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}


@Composable
fun ChildCodUI(
    navigator: Navigator?,
    state: ChildConfirmState,
    event: (ChildConfirmEvent)-> Unit
) {


    var code by remember { mutableStateOf("") }

    val formatted = remember(code) { formatCode(code) }

    val textFieldValue = remember(formatted) {
        TextFieldValue(
            text = formatted,
            selection = TextRange(formatted.length) // Kursor oxiriga
        )
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(BackgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            LogoHeader()

            CustomText(
                text = stringResource(Res.string.farzandingizni_tasdiqlang),
                fontSize = 28.sp,
                fontWeight = FontWeight.W500,
            )

            SpaceLarge()
            SpaceLarge()

            Image(
                painter = painterResource(Res.drawable.qr_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally)
            )

            SpaceLarge()
            SpaceLarge()

            CodeInputField(
                value = textFieldValue,
                onValueChange = { input ->
                    val digits = input.text.filter { it.isDigit() }.take(6)
                    code = digits
                }
            )

            SpaceSmall()

            CustomText(
                text = stringResource(Res.string.ushbu_kodni_farzandingiz_telefonidan_kiriting),
                fontSize = UltraSmallTextSize,
                color = HintTextColor
            )


            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {

                    Text(
                        text = stringResource(Res.string.hozir_emas),
                        fontSize = NormalTextSize,
                        color = PrimaryColor
                    )
                }
            }

            CustomButton(
                onClick = {
                    navigator?.push(MainScreen())
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = stringResource(Res.string.davom_etish)
            )
        }
    }
}

