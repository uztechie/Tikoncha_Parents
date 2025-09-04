package uz.tikoncha_parent.presentation.profile.coins

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
import androidx.compose.material3.MaterialTheme
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
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.presentation.profile.subscription.PaymentScreen
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bitta_tanga
import tikoncha_parents.composeapp.generated.resources.coin
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.hammasi
import tikoncha_parents.composeapp.generated.resources.money_light
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.sotib_olish
import tikoncha_parents.composeapp.generated.resources.tangachalar
import tikoncha_parents.composeapp.generated.resources.tangachalar_orqali
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText

class CoinsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        AppSettings.hasUserLogin = false

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
        title = stringResource(Res.string.farzandlaringiz),
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomHeader(
            title = stringResource(Res.string.tangachalar),
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
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(ContainerPadding)
            ) {

                CustomText(
                    text = stringResource(Res.string.tangachalar),
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = PrimaryColor,
                    fontSize = NormalLargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                SpaceUltraSmall()

                CustomText(
                    text = stringResource(Res.string.tangachalar_orqali),
                    color = MaterialTheme.colorScheme.secondary,
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
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
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
                    text = stringResource(Res.string.tangachalar),
                    fontSize = NormalTextSize,
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
                    .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(
                        TextFieldCornerRadius
                    ))
                    .padding(4.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(TextFieldCornerRadius),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
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
                                    .background(MaterialTheme.colorScheme.background)
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
                                text = stringResource(Res.string.bitta_tanga),
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
                        text = stringResource(Res.string.hammasi),
                        fontSize = NormalTextSize,
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
                onClick = {
                    navigator!!.push(PaymentScreen(coinsAmount = coinsAmount.toInt()))
                },
                text = stringResource(Res.string.sotib_olish),
                enabled = true,
                fontSize = NormalLargeTextSize
            )
            SpaceLarge()
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