package uz.tikoncha_parent.presentation.profile.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.common.*
import uz.tikoncha_parent.presentation.domain.model.Subscription
import uz.tikoncha_parent.presentation.profile.coins.CoinsScreen
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import kotlin.text.set

class SubscriptionScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        SubscriptionUi(
            navigator = navigator
        )
    }
}

@Composable
fun SubscriptionUi(
    navigator: Navigator?
) {

    val title = stringResource(Res.string.yillik)
    val title2 = stringResource(Res.string.oylik)

    val childrenList = remember {
        mutableStateListOf(
            "Saidburxon",
            "Muhammadsaid",
            "Muhammadyusuf",
            "Beka"
        )
    }

    val subscriptions = remember {
        mutableStateMapOf(
            0 to Subscription(
                title = title,
                price = 1500000,
                isSelected = false
            ),
            1 to Subscription(
                title = title2,
                price = 150000,
                isSelected = false
            )
        )
    }

    val selectedChildren = remember {
        mutableStateOf(childrenList[0])
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var isSelected by remember { mutableStateOf(false) }

    val selectedPrice = subscriptions.values.find { it.isSelected }?.price

    CustomListDialog(
        title = stringResource(Res.string.farzandingiz),
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
            title = stringResource(Res.string.obuna),
            showBackButton = true,
            onBackClick = {
                navigator!!.pop()
            },
            fonWeight = FontWeight.W600
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
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

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(TextFieldCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row {

                        CustomText(
                            text = stringResource(Res.string.xozir_sizning_obunangiz),
                            fontSize = NormalLargeTextSize,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(Modifier.size(5.dp))

                        CustomText(
                            text = stringResource(Res.string.standart),
                            fontSize = NormalLargeTextSize,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryColor
                        )
                    }

                    SpaceUltraSmall()

                    CustomText(
                        text = stringResource(Res.string.ilovaning_barcha_funksiyalaridan),
                        fontSize = NormalTextSize,
                        color = MaterialTheme.colorScheme.secondary,
                        style = TextStyle()
                    )
                }
            }

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1.8f)
                ) {
                    CustomText(
                        text = stringResource(Res.string.pro),
                        color = PrimaryColor,
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    CustomText(
                        color = MaterialTheme.colorScheme.secondary,
                        text = stringResource(Res.string.premium_obuna_bilan),
                        fontSize = NormalTextSize,
                        style = TextStyle()
                    )
                }

                CustomOutlinedButton(
                    text = stringResource(Res.string.tangachalar),
                    onClick = {
                        navigator!!.push(CoinsScreen())
                    },
                    textColor = PrimaryColor,
                    modifier = Modifier
                        .weight(1f)
                        .height(DialogButtonHeight)
                )
            }

            SpaceMedium()

            subscriptions.forEach { (index, subscription) ->

                SpaceMedium()

                SubscriptionOptionItem(
                    title = subscription.title,
                    priceUsd = subscription.price.toString(),
                    isSelected = subscription.isSelected,
                    onCheckedChange = { selected ->
                        subscriptions.forEach { (i, sub) ->
                            subscriptions[i]?.copy(isSelected = i == index)
                        }
                    },
                    fontWeight = FontWeight.W500
                )

                if (subscription.isSelected){
                    isSelected = true
                }
            }

            SpaceMedium()

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(TextFieldCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                SubscriptionItem(
                    selectedSubscription = SubscriptionType.COIN,
                    onSubscriptionSelected = {}
                )
            }

            Spacer(Modifier.weight(1f))

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = stringResource(Res.string.sotib_olish),
                enabled = isSelected,
                fontSize = NormalLargeTextSize,
                onClick = {
                    if (selectedPrice != null) {
                        navigator?.push(PaymentScreen(subscriptionPrice = selectedPrice))
                    }
                }
            )
            SpaceMedium()
        }
    }
}

@Preview
@Composable
fun PreviewSubscriptionScreen() {
    SubscriptionUi(
        navigator = null
    )
}