package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class CoinsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current?:return

        val viewModel = koinViewModel<MyCoinsViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

//        AppSettings.hasUserLogin = false

        LaunchedEffect(Unit){
            viewModel.loadCoinsPackages()
        }
        LaunchedEffect(Unit){
            event(CoinsEvent.GetChildren)
        }

        CoinsUi(
            navigator = navigator,
            state = state.value,
            event = event
        )

    }
}

@Composable
fun CoinsUi(
    navigator: Navigator?,
    state: MyCoinsState,
    event: (CoinsEvent) -> Unit
) {

    var coinsAmount by remember {
        mutableStateOf("1")
    }

    var showDialog by remember { mutableStateOf(false) }
    val childrenLoading = state.childrenResponseState is ResponseState.Loading
    val childrenErrorText = state.childrenResponseState.errorText()

    CustomListDialog(
        title = stringResource(Res.string.farzandlaringiz),
        items = state.childrenList,
        show = showDialog,
        loading = childrenLoading,
        errorMessage = childrenErrorText,
        onItemSelected = {
            event(CoinsEvent.OnChildSelected(it))
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
            },
            trailingIcon = {
                ChildSelectionButton(
                    modifier = Modifier
                        .widthIn(120.dp, 160.dp),
                    text = state.selectedChild?.name?:"",
                    imageUrl = state.selectedChild?.avatarUrl?:"",
                    label = stringResource(Res.string.farzandingizni_tanlang),
                    onClick = {
                        showDialog = true
                    },
                )
            }
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

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
                    .zIndex(1f)
                    .fillMaxWidth()
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

            SpaceMedium()

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.packages.forEach { pac ->
                    CoinPackItem(
                        coins = pac.coins,
                        price = pac.price,
                        discountPercent = pac.discountPercent,
                        onClick = {}
                    )
                }
            }
            SpaceMedium()
        }
    }
}

@Preview
@Composable
private fun PreviewCoinsScreen() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        CoinsUi(
            navigator = null,
            state = MyCoinsState(),
            event = {}
        )
    }
}