@file:Suppress("DEPRECATION")

package org.example.project.presentation.add_child

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import org.example.project.presentation.base.LogoHeader
import org.example.project.ui.PrimaryColor
import org.example.project.presentation.child_confirm_cod.ChildConfirmCodeRegisterScreen
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.main.MainScreen

class AddChildRegisterScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel = koinViewModel<ChildViewmodel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        AddChildRegisterUi(
            navigator = navigator,
            childState = state.value,
            childEvent = event
        )
    }
}


@Composable
fun AddChildRegisterUi(
    navigator: Navigator?,
    childState: ChildState,
    childEvent: (ChildEvent) -> Unit
) {

    var children by remember { mutableStateOf(listOf(ChildState())) }


    val areAllPhoneNumbersValid = children.all { child ->
        child.number.length == 9 && child.number.all { it.isDigit() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            LogoHeader()

            CustomText(
                text = stringResource(Res.string.xush_kelibsiz),
                fontSize = 28.sp,
                fontWeight = FontWeight.W500,
            )

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.farzandlaringiz),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.W500
            )

            SpaceMedium()

            // Barcha farzand telefon raqamlarini ko‘rsatish
            children.forEachIndexed { index, childState ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(width = 1.dp, color = if (childState.accept) PrimaryColor else BorderColor, shape = RoundedCornerShape(TextFieldCornerRadius)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
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
                            phoneNumber = childState.number,
                            onPhoneNumberChange = { newNumber ->
                                children = children.toMutableList().also {
                                    it[index] = it[index].copy(number = newNumber)
                                }
                            },
                            isAccepted = childState.accept
                        )
                    }
                }
            }

            SpaceLarge()

            // ➕ Farzand qo‘shish tugmasi
            TextButton(
                onClick = {
                    children = children + ChildState()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {
                    Text(
                        text = stringResource(Res.string.farzand_qo_shish),
                        fontSize = NormalTextSize,
                        color = PrimaryColor
                    )

                    SpaceMedium()

                    Icon(
                        painter = painterResource(Res.drawable.add_square),
                        contentDescription = "",
                        tint = PrimaryColor
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = {
                    navigator?.replaceAll(MainScreen())
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(TextFieldCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {

                    CustomText(
                        text = stringResource(Res.string.o_tkazib_yuborish),
                        fontSize = NormalTextSize,
                        color = PrimaryColor,
                        fontWeight = FontWeight.W500,
                    )
                }
            }

            CustomButton(
                onClick = {

                    children = children.map { it.copy(accept = true) }

                    navigator?.push(ChildConfirmCodeRegisterScreen())
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = areAllPhoneNumbersValid,
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
    AddChildRegisterUi(
        navigator = null,
        childState = ChildState(),
        childEvent = {}
    )
}