package org.example.project.presentation.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.common.CustomText
import org.example.project.presentation.profile.CustomHeader
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.click_pay
import tikoncha_parents.composeapp.generated.resources.money_light
import tikoncha_parents.composeapp.generated.resources.pay_me
import tikoncha_parents.composeapp.generated.resources.profile

class PaymentScreen(private val selectedPrice: String) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        PaymentScreenUi(
            navigator = navigator,
            selectedPrice = selectedPrice
        )
    }
}

@Composable
fun PaymentScreenUi(
    navigator: Navigator?,
    selectedPrice: String
) {

    var selectedPayment by remember { mutableStateOf("") }

    var isSelected by remember { mutableStateOf(false) }

    if (selectedPayment.isNotEmpty()){
        isSelected = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(2.dp)
    ) {

        CustomHeader(
            title = "Tasdiqlash",
            showBackButton = true,
            onBackClick = {
                navigator!!.pop()
            },
            fonWeight = FontWeight.W500,
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SpaceMedium()

            CustomText(
                text = "To’lov",
                fontSize = UltraLargeTextSize,
                fontWeight = FontWeight.W600
            )

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PaymentOptionKMP(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(Res.drawable.pay_me),
                    isSelected = selectedPayment == "payme",
                    onClick = { selectedPayment = "payme" }
                )
                PaymentOptionKMP(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(Res.drawable.click_pay),
                    isSelected = selectedPayment == "click",
                    onClick = { selectedPayment = "click" }
                )
            }



            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardColors, RoundedCornerShape(30.dp))
                    .padding(4.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = CardColors),
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center, // ⬅️ Vertikal markaz
                    ) {

                    Row(
                        modifier = Modifier.padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(BackgroundColor)
                                .padding(10.dp)
                        ){
                            Image(
                                painter = painterResource(Res.drawable.money_light),
                                contentDescription = null,
                                modifier = Modifier.size(NormalIconButtonSize)
                            )
                        }

                        SpaceMedium()

                        CustomText(
                            text = "Obuna Pro 150 ming so’m",
                            fontSize = NormalTextSizeSp,
                            fontWeight = FontWeight.W500,
                        )
                    }
                        }

                    SpaceUltraSmall()

                }
                SpaceLarge()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ContainerPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    CustomText(
                        text = "Hammasi",
                        fontSize = NormalTextSize,
                        color = TextColor,
                        fontWeight = FontWeight.W600
                    )
                    CustomText(
                        text = "$selectedPrice UZS",
                        fontSize = NormalTextSize,
                        color = PrimaryColor,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            SpaceMedium()

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = "Sotib olish",
                enabled = isSelected,
                fontSize = NormalLargeTextSize,
                onClick = { }
            )

            SpaceMedium()

        }
    }
}

@Composable
@Preview
private fun Preview() {
    PaymentScreenUi(
        navigator = null,
        selectedPrice = ""
    )
}