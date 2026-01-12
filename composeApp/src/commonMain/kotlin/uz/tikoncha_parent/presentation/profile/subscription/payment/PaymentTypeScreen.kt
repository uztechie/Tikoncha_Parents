package uz.tikoncha_parent.presentation.profile.subscription.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.model.PaymentStatus
import uz.tikoncha_parent.domain.model.SubscriptionDuration
import uz.tikoncha_parent.platform.isIos
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomPaymentDialog
import uz.tikoncha_parent.presentation.base.LegalLinksRow
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class PaymentTypeScreen(
    val subDuration: SubscriptionDuration,
    val amount: Int,
    val planId: String
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        
        val viewModel = koinScreenModel<PaymentViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(Unit) {
            event(PaymentEvent.SetSubscriptionDuration(subDuration))
            event(PaymentEvent.SetAmount(amount = amount))
            event(PaymentEvent.SetPlanId(planId))
        }

        PaymentTypeScreenUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}

@Composable
fun PaymentTypeScreenUi(
    navigator: Navigator?,
    state: PaymentState = PaymentState(),
    event: (PaymentEvent) -> Unit = {}
) {

    var selectedPayment by remember { mutableStateOf("") }

    var isSelected by remember { mutableStateOf(false) }

    if (selectedPayment.isNotEmpty()) {
        isSelected = true
    }


    val paymentLoading = state.paymentResponseState is ResponseState.Loading
    val paymentError = state.paymentResponseState.errorText()

    val applePaymentLoading = state.applePaymentResponseState is ResponseState.Loading
    val applePaymentError = state.applePaymentResponseState.errorText()
    val applePaymentSuccess = state.applePaymentResponseState is ResponseState.Success
    LoadingDialog(paymentLoading || applePaymentLoading)


    var showCreatePaymentErrorDialog by remember() {
        mutableStateOf(false)
    }

    var showAppleCreatePaymentErrorDialog by remember() {
        mutableStateOf(false)
    }

    LaunchedEffect(paymentError) {
        if (paymentError.isNotEmpty()) {
            showCreatePaymentErrorDialog = true
        }
    }

    LaunchedEffect(applePaymentError) {
        if (applePaymentError.isNotEmpty()) {
            showAppleCreatePaymentErrorDialog = true
        }
    }

    var showPaymentCompletedDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.paymentStatus) {
        showPaymentCompletedDialog = state.paymentStatus == PaymentStatus.COMPLETED
    }

    LaunchedEffect(applePaymentSuccess){
        if (applePaymentSuccess && AppSettings.isTestAccount){
            navigator?.pop()
        }
    }


    CustomPaymentDialog(
        show = showPaymentCompletedDialog,
        onDismiss = {
            showPaymentCompletedDialog = false
            event(PaymentEvent.ResetPaymentResponse)
            navigator?.pop()
        },
        onButtonClick = {
            showPaymentCompletedDialog = false
            event(PaymentEvent.ResetPaymentResponse)
            navigator?.pop()
        }
    )


    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showCreatePaymentErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = paymentError,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showCreatePaymentErrorDialog = false
            event(PaymentEvent.ResetPaymentResponse)
        },
        onButtonClick = {
            showCreatePaymentErrorDialog = false
            event(PaymentEvent.ResetPaymentResponse)
        }
    )

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showAppleCreatePaymentErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = applePaymentError,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showAppleCreatePaymentErrorDialog = false
            event(PaymentEvent.ResetPaymentResponse)
        },
        onButtonClick = {
            showAppleCreatePaymentErrorDialog = false
            event(PaymentEvent.ResetPaymentResponse)
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            title = stringResource(Res.string.tasdiqlash),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
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
                text = stringResource(Res.string.tolov),
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
                if (isIos() && state.isTestAccount) {
                    PaymentOption(
                        modifier = Modifier.weight(1f),
                        paymentType = PaymentType.AppStore,
                        isSelected = state.selectedPaymentType == PaymentType.AppStore,
                        onClick = {
                            event(PaymentEvent.SetPaymentType(PaymentType.AppStore))
                        }
                    )
                }
                PaymentOption(
                    modifier = Modifier.weight(1f),
                    paymentType = PaymentType.Click,
                    isSelected = state.selectedPaymentType == PaymentType.Click,
                    onClick = {
                        event(PaymentEvent.SetPaymentType(
                            PaymentType.Click
                        ))
                    }
                )

                if (!isIos()) {
                    Box(
                        modifier = Modifier.weight(1f),
                    )
                }

            }

            SpaceMedium()

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(72.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                PaymentOption(
//                    modifier = Modifier.weight(1f),
//                    painter = painterResource(Res.drawable.paynet),
//                    isSelected = false,
//                    onClick = { selectedPayment = "paynet" }
//                )
//                PaymentOption(
//                    modifier = Modifier.weight(1f),
//                    painter = painterResource(Res.drawable.uzum),
//                    isSelected = false,
//                    onClick = { selectedPayment = "uzum" }
//                )
//            }
//            SpaceMedium()

            val paymentType = when(state.selectedPaymentType){
                PaymentType.Click -> "Click"
                PaymentType.AppStore -> "App Store"
                null -> ""
            }
            CustomText(
                text = stringResource(Res.string.payment_type_text, paymentType),
                fontSize = NormalTextSize,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500
            )

            SpaceMedium()


            val priceText = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.extendedColor.primaryColor,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(state.amount.toCurrency())
                }
                append(" /")
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.extendedColor.textColor,
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    val text = if (state.subscriptionDuration == SubscriptionDuration.MONTHLY){
                        stringResource(Res.string.oylik)
                    }
                    else{
                        stringResource(Res.string.yillik)
                    }
                    append(text = text)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.extendedColor.cardColor,
                        RoundedCornerShape(TextFieldCornerRadius)
                    )
                    .padding(16.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.tikoncha_plus),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    Spacer(Modifier.weight(1f))

                    CustomText(
                        text = priceText,
                    )
                }
                SpaceMedium()
                DividerHorizontal()
                SpaceMedium()

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = stringResource(Res.string.obuna_holati),
                        color = MaterialTheme.extendedColor.hintColor,
                        fontSize = NormalTextSize
                    )

                    Spacer(Modifier.weight(1f))

                    var paymentStatus = if (state.paymentStatus == PaymentStatus.COMPLETED){
                        stringResource(Res.string.tolandi)
                    }
                    else if (state.paymentStatus == PaymentStatus.PENDING){
                        stringResource(Res.string.tolov_kutilmoqda)
                    }
                    else{
                        stringResource(Res.string.tolanmagan)
                    }

                    val paymentStatusColor = if (state.paymentStatus == PaymentStatus.COMPLETED){
                        MaterialTheme.extendedColor.primaryColor
                    }
                    else if (state.paymentStatus == PaymentStatus.PENDING){
                        ImportantButtonColor

                    }
                    else{
                        OtpErrorColor
                    }

                    if (state.paymentStatus == PaymentStatus.PENDING){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(SmallIconSize),
                            color = PrimaryColor,
                            trackColor = PrimaryColor.copy(alpha = 0.3f),
                        )
                        SpaceUltraSmall()
                    }

                    CustomText(
                        text = paymentStatus,
                        color = paymentStatusColor,
                        fontSize = NormalTextSize
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
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
                                text =  "${state.amount.toCurrency()} UZS",
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
                text = stringResource(Res.string.sotib_olish),
                enabled = state.paymentStatus != PaymentStatus.PENDING && state.selectedPaymentType != null,
                fontSize = NormalLargeTextSize,
                onClick = {
                    event(PaymentEvent.Pay)
                }
            )
            SpaceLarge()
            LegalLinksRow()
            SpaceUltraSmall()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(ThemeMode.LIGHT){
        PaymentTypeScreenUi(
            navigator = null,
            state = PaymentState(
                amount = 199000,
                subscriptionDuration = SubscriptionDuration.ANNUAL,
                isTestAccount = true,
                selectedPaymentType = PaymentType.AppStore
            ),
            event = {}
        )
    }

}