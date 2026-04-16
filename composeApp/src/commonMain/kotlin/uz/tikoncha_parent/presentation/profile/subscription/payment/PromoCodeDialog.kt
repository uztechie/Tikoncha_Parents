package uz.tikoncha_parent.presentation.profile.subscription.payment

import PromoCodeInput
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.promokod_tasdiqlandi
import tikoncha_parents.composeapp.generated.resources.promokodni_kiriting
import tikoncha_parents.composeapp.generated.resources.qo_llash
import tikoncha_parents.composeapp.generated.resources.tekshirilmoqda
import tikoncha_parents.composeapp.generated.resources.yopish
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomLoadingButton
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.coverShadow
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.UltraSmallTextSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun PromoCodeDialog(
    show: Boolean,
    state: PaymentState,
    event: (PaymentEvent) -> Unit,
    onDismiss: () -> Unit
) {


    if (!show) return

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val errorMessage = state.promoCodeResponseState.errorText()
    val loading = state.promoCodeResponseState is ResponseState.Loading
    val success = state.promoCodeResponseState is ResponseState.Success

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(Unit) {
        event(PaymentEvent.ClearPromoCodeResponse)
    }

    LaunchedEffect(success) {
        if (success){
            delay(2000)
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .coverShadow(
                    shape = RoundedCornerShape(CardCornerRadius)
                )
                .fillMaxWidth()
                .background(
                    MaterialTheme.extendedColor.backgroundColor, RoundedCornerShape(
                        CardCornerRadius
                    )
                )
                .padding(25.dp)
        ) {

            CustomText(
                text = stringResource(Res.string.promokodni_kiriting),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            SpaceLarge()


            PromoCodeInput(
                promoCode = state.promoCode,
                onPromoCodeChange = { event(PaymentEvent.OnPromoCode(it)) },
                isError = errorMessage.isNotEmpty(),
                errorMessage = errorMessage,
                focusRequester = focusRequester,

                containerColor = MaterialTheme.extendedColor.cardColor,
                textColor = MaterialTheme.extendedColor.textColor,
                cursorColor = MaterialTheme.extendedColor.textColor,
                errorContainerColor = OtpErrorColor.copy(alpha = 0.15f),
                errorTextColor = OtpErrorColor,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight)
            )


            SpaceUltraSmall()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = TextFieldCornerRadius)
            )
            {
                if (errorMessage.isNotEmpty()) {
                    CustomText(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = errorMessage,
                        color = OtpErrorColor,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,
                        fontSize = UltraSmallTextSize
                    )
                }
                else if (success) {
                    CustomText(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(Res.string.promokod_tasdiqlandi),
                        color = PrimaryColor,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,
                        fontSize = UltraSmallTextSize
                    )
                }
                else{
                    CustomText(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "",
                        color = PrimaryColor,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,
                        fontSize = UltraSmallTextSize
                    )
                }
            }



            SpaceLarge()
            SpaceLarge()

            val buttonLabel = when (loading) {
                true -> {
                    stringResource(Res.string.tekshirilmoqda)
                }

                false -> {
                    stringResource(Res.string.qo_llash)
                }
            }


            if (success) {
                CustomButton(
                    fontSize = SmallTextSize,
                    text = stringResource(Res.string.yopish),
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DialogButtonHeight)
                )
            } else {
                CustomLoadingButton(
                    fontSize = SmallTextSize,
                    loading = loading,
                    enabled = !loading && state.promoCode.isNotEmpty(),
                    text = buttonLabel,
                    onClick = {
                        event(PaymentEvent.ValidatePromoCode)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DialogButtonHeight)
                )

                SpaceMedium()
                CustomOutlinedButton(
                    fontSize = SmallTextSize,
                    enabled = !loading,
                    text = stringResource(Res.string.bekor_qilish),
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DialogButtonHeight)
                )
            }


        }
    }

}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(mode = ThemeMode.LIGHT){
        PromoCodeDialog(
            show =  true,
            state = PaymentState(
                promoCode = "fd",
                promoCodeResponseState = ResponseState.Success()
            ),
            event = {},
            onDismiss = {}
        )
    }

}
