package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.common.*
import uz.tikoncha_parent.presentation.domain.model.Subscription
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.presentation.base.tripleShadow
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentTypeScreen
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class SubscriptionPaymentScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        SubscriptionPaymentUi(
            navigator = navigator,
            state = SubscriptionPaymentState()
        )
    }
}

@Composable
fun SubscriptionPaymentUi(
    navigator: Navigator?,
    state: SubscriptionPaymentState = SubscriptionPaymentState()
) {

    var showButtonSheetState by remember { mutableStateOf(false) }

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

    var isSelected = subscriptions.values.any { it.isSelected }

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
            .background(MaterialTheme.extendedColor.backgroundColor)
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .tripleShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                    )
                    .background(
                        MaterialTheme.extendedColor.cardColor,
                        RoundedCornerShape(CardCornerRadius)
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(NormalIconButtonSize)
                                .clip(RoundedCornerShape(ShapeCornerRadius))
                                .background(MaterialTheme.extendedColor.backgroundColor),
                            contentAlignment = Alignment.Center
                        ) {
                            val painter = when (state.currentPlan) {
                                SubscriptionType.FREE -> {
                                    painterResource(Res.drawable.telegrams_star)
                                }

                                SubscriptionType.PLUS -> {
                                    painterResource(Res.drawable.crown)
                                }

                                SubscriptionType.PRO -> {
                                    painterResource(Res.drawable.crown)
                                }
                            }

                            Icon(
                                painter = painter,
                                contentDescription = "",
                                tint = PrimaryColor,
                                modifier = Modifier
                                    .fillMaxSize(0.6f)
                            )
                        }
                        SpaceSmall()
                        Column {
                            CustomText(
                                text = stringResource(Res.string.xozr_sizning_obunangiz),
                                fontSize = SmallTextSize,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.extendedColor.hintColor
                            )

                            val currentPlan = when (state.currentPlan) {
                                SubscriptionType.FREE -> {
                                    stringResource(Res.string.standart)
                                }

                                SubscriptionType.PLUS -> {
                                    "PLUS"
                                }

                                SubscriptionType.PRO -> {
                                    "PRO"
                                }
                            }

                            CustomText(
                                text = currentPlan,
                                fontSize = NormalTextSize,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryColor
                            )
                        }
                    }

                    SpaceSmall()

                    CustomText(
                        text = stringResource(Res.string.ilovaning_barcha_funksiyalaridan_foydalanish_uchun_pro_versiyasini_sotib_oling),
                        fontSize = SmallTextSize,
                        color = MaterialTheme.extendedColor.hintColor,

                    )
                }
            }

            SpaceMedium()
            CustomText(
                text = stringResource(Res.string.baxtli_foydalanuvchimisiz),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
            )
            SpaceMedium()
            if (state.subscriptionUi != null) {
                SubscriptionPaymentItem(
                    subscriptionUi = state.subscriptionUi,
                    onClick = {
                        showButtonSheetState = true
                    }
                )
            }


            Spacer(Modifier.weight(1f))

            SpaceMedium()

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = stringResource(Res.string.sotib_olish),
                enabled = isSelected,
                fontSize = NormalLargeTextSize,
                onClick = {
                    if (selectedPrice != null) {
                        navigator?.push(PaymentTypeScreen(subscriptionPrice = selectedPrice))
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
    TikonchaParentTheme(
        ThemeMode.DARK,
    ){
        SubscriptionPaymentUi(
            navigator = null,
            state = SubscriptionPaymentState()
        )
    }
}