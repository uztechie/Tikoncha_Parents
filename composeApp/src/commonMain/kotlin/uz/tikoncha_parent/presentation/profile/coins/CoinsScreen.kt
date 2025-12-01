package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.ColorFilter
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
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentTypeScreen
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.ContainerCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalLargeTextSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_down
import tikoncha_parents.composeapp.generated.resources.arrow_left
import tikoncha_parents.composeapp.generated.resources.arrow_pay
import tikoncha_parents.composeapp.generated.resources.arrow_previous
import tikoncha_parents.composeapp.generated.resources.arrow_right
import tikoncha_parents.composeapp.generated.resources.arrow_right_rounded
import tikoncha_parents.composeapp.generated.resources.arrow_up
import tikoncha_parents.composeapp.generated.resources.chegirma
import tikoncha_parents.composeapp.generated.resources.chegirmalar
import tikoncha_parents.composeapp.generated.resources.coin
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.money_light
import tikoncha_parents.composeapp.generated.resources.obuna_holati
import tikoncha_parents.composeapp.generated.resources.primary_arrow_right
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.sotib_olish
import tikoncha_parents.composeapp.generated.resources.tangachalar
import tikoncha_parents.composeapp.generated.resources.tangachalar_orqali
import tikoncha_parents.composeapp.generated.resources.tikoncha_plus
import tikoncha_parents.composeapp.generated.resources.tolandi
import tikoncha_parents.composeapp.generated.resources.tolanmagan
import tikoncha_parents.composeapp.generated.resources.tolov_kutilmoqda
import tikoncha_parents.composeapp.generated.resources.tolov_summasi
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.PaymentStatus
import uz.tikoncha_parent.presentation.base.tripleShadow
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.ImportantButtonColor
import uz.tikoncha_parent.ui.LargeIconButtonSize
import uz.tikoncha_parent.ui.LargeIconSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

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
            .background(MaterialTheme.extendedColor.backgroundColor)
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

//            CustomSelectionButton(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(TextFieldHeight),
//                text = selectedChildren.value,
//                painter = painterResource(Res.drawable.profile),
//                tint = MaterialTheme.extendedColor.primaryAlphaColor,
//                onClick = {
//                    showDialog = true
//                }
//            )

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
                        .background(MaterialTheme.extendedColor.cardColor),
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

            SpaceMedium()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(ContainerCornerRadius))
                    .background(MaterialTheme.extendedColor.cardColor)
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
                    color = MaterialTheme.extendedColor.hintColor,
                    fontSize = NormalTextSize,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle()
                )
            }
            
            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.chegirmalar),
                fontSize = NormalTextSize,
            )

            SpaceMedium()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.extendedColor.cardColor,
                        RoundedCornerShape(CardCornerRadius)
                    )
                    .padding(16.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(SmallIconButtonSize)
                            .clip(RoundedCornerShape(ShapeCornerRadius))
                            .background(MaterialTheme.extendedColor.backgroundColor),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(Res.drawable.coin),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize(0.7f)
                        )
                    }

                    SpaceSmall()
                    Column {
                        CustomText(
                            text = stringResource(Res.string.tangachalar),
                            fontSize = NormalTextSize,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.extendedColor.hintColor
                        )
                        CustomText(
                            text = "5 000 tanga"
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    CustomText(
                        text = "350 000 UZS",
                        fontSize = NormalTextSize,
                        color = PrimaryColor,
                        fontWeight = FontWeight.W600
                    )
                }
                SpaceMedium()
                DividerHorizontal()
                SpaceMedium()

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                            MaterialTheme.extendedColor.primaryColor,
                            RoundedCornerShape(TextFieldCornerRadius))
                            .padding(horizontal = 6.dp)
                    ){
                        CustomText(
                            text = "30%"
                        )
                    }
                    SpaceSmall()
                    CustomText(
                        text = stringResource(Res.string.chegirma),
                        color = MaterialTheme.extendedColor.hintColor,
                        fontSize = NormalTextSize,
                        modifier = Modifier.weight(1f)
                    )

                   Image(
                       painter = painterResource(Res.drawable.arrow_pay),
                       contentDescription = null,
                       modifier = Modifier
                           .size(LargeIconSize)
                           .background(PrimaryColor, CircleShape)
                           .padding(8.dp)
                   )
                }
            }

            SpaceLarge()

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(
//                        TextFieldCornerRadius
//                    ))
//                    .padding(4.dp)
//            ) {
//
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(64.dp),
//                    shape = RoundedCornerShape(TextFieldCornerRadius),
//                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.extendedColor.cardColor),
//                )
//                {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                    ) {
//
//                        Row(
//                            modifier = Modifier.padding(6.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .clip(CircleShape)
//                                    .background(MaterialTheme.colorScheme.background)
//                                    .padding(10.dp)
//                            ){
//                                Image(
//                                    painter = painterResource(Res.drawable.money_light),
//                                    contentDescription = null,
//                                    modifier = Modifier.size(NormalIconButtonSize)
//                                )
//                            }
//
//                            SpaceMedium()
//
//                            CustomText(
//                                text = stringResource(Res.string.bitta_tanga),
//                                fontSize = NormalTextSize,
//                                fontWeight = FontWeight.Medium,
//                            )
//                        }
//                    }
//                    SpaceUltraSmall()
//                }
//
//                SpaceLarge()
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = ContainerPadding),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                )
//                {
//                    CustomText(
//                        text = stringResource(Res.string.hammasi),
//                        fontSize = NormalTextSize,
//                        fontWeight = FontWeight.W600
//                    )
//
//                    CustomText(
//                        text = "${coinsAmount.toInt() * 100} UZS",
//                        fontSize = NormalTextSize,
//                        color = PrimaryColor,
//                        fontWeight = FontWeight.W600
//                    )
//                }
//            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .tripleShadow(CircleShape)
                    .background(MaterialTheme.extendedColor.cardColor, CircleShape)
                    .padding(6.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.extendedColor.backgroundColor),
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                    ) {

                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.extendedColor.cardColor,
                                        CircleShape
                                    )
                                    .padding(10.dp)
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.money_light),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryAlphaColor),
                                    modifier = Modifier
                                        .size(NormalIconSize)
                                )
                            }

                            SpaceMedium()

                            CustomText(
                                text = stringResource(Res.string.tolov_summasi),
                                fontSize = NormalTextSize,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(Modifier.weight(1f))
                            CustomText(
                                text =  "${coinsAmount.toInt() * 100} UZS",
                                fontSize = NormalTextSize,
                                color = PrimaryColor,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }

                }
            }

            SpaceLarge()

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                onClick = {
//                    navigator!!.push(PaymentTypeScreen(coinsAmount = coinsAmount.toInt(),))
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
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        CoinsUi(
            navigator = null
        )
    }
}