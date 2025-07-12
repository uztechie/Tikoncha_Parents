package org.example.project.presentation.profile.coins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.common.CustomListDialog
import org.example.project.presentation.common.CustomSelectionButton
import org.example.project.presentation.common.CustomText
import org.example.project.presentation.profile.CustomHeader
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.CardColors
import org.example.project.ui.ContainerCornerRadius
import org.example.project.ui.ContainerPadding
import org.example.project.ui.HintTextColor
import org.example.project.ui.LargeTextSize
import org.example.project.ui.NormalIconButtonSize
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SmallIconButtonSize
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.TextColor
import org.example.project.ui.TextFieldHeight
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coin
import tikoncha_parents.composeapp.generated.resources.money_light
import tikoncha_parents.composeapp.generated.resources.profile

class CoinsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        CoinsUi(
            navigator = navigator
        )

    }
}

@Composable
fun CoinsUi(
    navigator: Navigator?
) {

    var coinsAmount by remember {
        mutableStateOf("1")
    }

    val childrenList = remember {
        mutableStateListOf(
            "Saidburxon",
            "Muhammadsaid",
            "Muhammadyusuf",
            "Beka"
        )
    }

    val selectedChildren = remember {
        mutableStateOf(childrenList[0])
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    CustomListDialog(
        title = "Farzandlaringiz",
        items = childrenList,
        show = showDialog,
        onItemSelected = { child ->
            selectedChildren.value = child
        },
        onDismiss = {
            showDialog = false
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = "Tangachalar",
            showBackButton = true,
            onBackClick = {
                navigator!!.pop()
            }
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .imePadding()
        ) {

            CustomSelectionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                text = selectedChildren.value,
                painter = painterResource(Res.drawable.profile),
                onClick = {
                    showDialog = true
                }
            )

            SpaceMedium()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(ContainerCornerRadius))
                    .background(TonalButtonContainerColor)
                    .padding(ContainerPadding)
            ) {

                CustomText(
                    text = "Tangachalar",
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = PrimaryColor,
                    fontSize = NormalLargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                SpaceUltraSmall()

                CustomText(
                    text = "Tangachalar orqali farzandingiz Suniy intellekt imkoniyatlaridan foydalanishi mumkin",
                    color = HintTextColor,
                    fontSize = NormalTextSize,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle()
                )

            }

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(SmallIconButtonSize)
                        .clip(RoundedCornerShape(ShapeCornerRadius))
                        .background(TonalButtonContainerColor),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painterResource(Res.drawable.coin),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(0.7f)
                    )
                }

                SpaceUltraSmall()

                CustomText(
                    text = "Tangachalar",
                    fontSize = NormalTextSize,
                    color = TextColor,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                CoinAmountTextField(
                    coinsAmount = coinsAmount,
                    onValueChange = {
                        coinsAmount = it
                    },
                    onAddCoinClicked = {
                        coinsAmount = it.toString()
                    },
                    onSubtractButtonClicked = {
                        coinsAmount = it.toString()
                    }
                )

            }

            SpaceLarge()

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

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
                        verticalArrangement = Arrangement.Center,
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
                                text = "Bitta tanga 100 so'm",
                                fontSize = NormalTextSize,
                                fontWeight = FontWeight.Medium,
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
                        text = "${coinsAmount.toInt() * 100}.00 UZS",
                        fontSize = NormalTextSize,
                        color = PrimaryColor,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            SpaceLarge()

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                onClick = {},
                text = "Sotib olish",
                enabled = true,
                fontSize = LargeTextSize
            )

        }
    }
}

@Preview
@Composable
fun PreviewCoinsScreen() {
    CoinsUi(
        navigator = null
    )
}