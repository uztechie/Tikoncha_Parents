package uz.tikoncha_parent.presentation.profile.subscription.payment

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
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
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.domain.model.PaymentStatus
import uz.tikoncha_parent.domain.model.SubscriptionDuration
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.tripleShadow
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class PaymentTypeScreen(
    private val subscriptionPrice: Int? = null,
    private val coinsAmount: Int? = null
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        
        val viewModel = koinViewModel<PaymentViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        PaymentTypeScreenUi(
            navigator = navigator,
            subscriptionPrice = subscriptionPrice,
            coinsAmount = coinsAmount,
            state = state,
            event = event
        )
    }
}

@Composable
fun PaymentTypeScreenUi(
    navigator: Navigator?,
    subscriptionPrice: Int? = null,
    coinsAmount: Int? = null,
    state: PaymentState = PaymentState(),
    event: (PaymentEvent) -> Unit = {}
) {

    var selectedPayment by remember { mutableStateOf("") }

    var isSelected by remember { mutableStateOf(false) }

    if (selectedPayment.isNotEmpty()) {
        isSelected = true
    }


    val loading = state.responseState is ResponseState.Loading
    val errorText = state.responseState.errorText()
    val success = state.responseState is ResponseState.Success


    LoadingDialog(loading)

    var showErrorDialog by remember() {
        mutableStateOf(false)
    }
    var showSuccessDialog by remember() {
        mutableStateOf(false)
    }

    LaunchedEffect(errorText){
        if (errorText.isNotEmpty()){
            showErrorDialog = true
        }
    }
    LaunchedEffect(success){
        if (success){

        }
    }

    CustomDialog(
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = errorText,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showErrorDialog = false
        },
        onButtonClick = {
            showErrorDialog = false
        }
    )

    CustomDialog(
        show = showSuccessDialog,
        title = stringResource(Res.string.muvaffaqiyatli),
        message = stringResource(Res.string.jadval_muvaffaqiyatli_yaratildi),
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showSuccessDialog = false
        },
        onButtonClick = {
            showSuccessDialog = false
            navigator?.pop()
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
                navigator!!.pop()
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
                PaymentOption(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(Res.drawable.pay_me),
                    isSelected = selectedPayment == "payme",
                    onClick = { selectedPayment = "payme" }
                )
                PaymentOption(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(Res.drawable.click_pay),
                    isSelected = selectedPayment == "click",
                    onClick = { selectedPayment = "click" }
                )
            }

            SpaceMedium()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PaymentOption(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(Res.drawable.paynet),
                    isSelected = selectedPayment == "paynet",
                    onClick = { selectedPayment = "paynet" }
                )
                PaymentOption(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(Res.drawable.uzum),
                    isSelected = selectedPayment == "uzum",
                    onClick = { selectedPayment = "uzum" }
                )
            }
            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.tolov_click_platformasi_orqali_amalga_oshiriladi),
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
                    .tripleShadow(RoundedCornerShape(TextFieldCornerRadius))
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
                                text =  "${state.amount.toCurrency()} UZS",
                                fontSize = NormalTextSize,
                                color = PrimaryColor,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }

                }
            }

//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .border(1.dp, MaterialTheme.extendedColor.borderColor, RoundedCornerShape(TextFieldCornerRadius))
//                    .padding(4.dp)
//            )
//            {
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
//                        verticalArrangement = Arrangement.Center, // ⬅️ Vertikal markaz
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
//                                    .background(MaterialTheme.extendedColor.tonalButtonColor)
//                                    .padding(10.dp)
//                            ) {
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
//                                text = if (subscriptionPrice != null) {
//                                    if (subscriptionPrice < 1000000){
//                                        "${stringResource(Res.string.obuna_pro)} ${subscriptionPrice / 1000} ${stringResource(Res.string.ming_sum)}"
//                                    }else{
//
//                                        val millions = subscriptionPrice / 1000000
//                                        val thousands = (subscriptionPrice - (millions * 1000000)) / 1000
//
//                                        "${stringResource(Res.string.obuna_pro)} $millions ${stringResource(Res.string.million)} $thousands ${stringResource(Res.string.ming_sum)}"
//                                    }
//                                }else "${coinsAmount.toString()} ${stringResource(Res.string.ta_tanga)}",
//                                fontSize = NormalTextSizeSp,
//                                fontWeight = FontWeight.W500,
//                            )
//                        }
//                    }
//
//                    SpaceUltraSmall()
//
//                }
//
//                SpaceLarge()
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
//                    CustomText(
//                        text = if (subscriptionPrice != null) "${subscriptionPrice}.00 UZS" else "${coinsAmount!! * 100}.00 UZS",
//                        fontSize = NormalTextSize,
//                        color = PrimaryColor,
//                        fontWeight = FontWeight.W600
//                    )
//                }
//            }

            SpaceLarge()

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = stringResource(Res.string.sotib_olish),
                enabled = isSelected,
                fontSize = NormalLargeTextSize,
                onClick = {
                    event(PaymentEvent.Purchase)
                }
            )
            SpaceLarge()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(ThemeMode.LIGHT){
        PaymentTypeScreenUi(
            navigator = null,
            coinsAmount = 1000
        )
    }

}