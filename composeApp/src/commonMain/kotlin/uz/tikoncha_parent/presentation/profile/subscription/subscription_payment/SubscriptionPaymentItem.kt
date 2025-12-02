package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import org.jetbrains.compose.resources.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.presentation.profile.subscription.PlanUi
import uz.tikoncha_parent.presentation.profile.subscription.SubscriptionUi
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*

@Composable
fun SubscriptionPaymentItem(
    modifier: Modifier = Modifier,
    subscriptionUi: SubscriptionUi,
    imagePay: SubscriptionType = SubscriptionType.FREE,
    onClick: () -> Unit,
) {

    val titleIcon = when (imagePay) {
        SubscriptionType.FREE -> painterResource(Res.drawable.tikoncha_plus)
        SubscriptionType.PLUS -> painterResource(Res.drawable.tikoncha_plus)
        SubscriptionType.PRO -> painterResource(Res.drawable.tikoncha_pro)
        else -> {
            painterResource(Res.drawable.tikoncha_plus)
        }
    }

    val monthlyPrise = buildAnnotatedString {
        append(stringResource(Res.string.oylik))
        append("/ ")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.extendedColor.primaryColor,
                fontWeight = FontWeight.W600,
                fontSize = NormalTextSize
            )
        ) {
            append("${subscriptionUi.monthly.price.toCurrency()}")
        }
    }

    val annualPrise = buildAnnotatedString {
        append(stringResource(Res.string.yillik))
        append("/ ")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.extendedColor.primaryColor,
                fontWeight = FontWeight.W600,
                fontSize = NormalTextSize
            )
        ) {
            append("${subscriptionUi.annual.price.toCurrency()}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .clip(RoundedCornerShape(CardCornerRadius))
            .background(MaterialTheme.extendedColor.cardColor)
            .padding(ContainerPadding)
    ) {
        Image(
            painter = titleIcon,
            contentDescription = "clock",
        )

        SpaceSmall()
        DividerHorizontal(
            modifier = Modifier
                .padding(vertical = ContainerPadding / 2)
        )
        SpaceSmall()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            SpaceSmall()

            Column(
                modifier = Modifier.weight(1f)
            ) {
                CustomText(
                    text = monthlyPrise,
                    fontSize = NormalTextSize,
                    color = MaterialTheme.extendedColor.hintColor,
                    lineHeight = SmallTextSize
                )
                CustomText(
                    text = "+${stringResource(Res.string.tanga, subscriptionUi.monthly.coin)}",
                    fontSize = SmallTextSize,
                    color = MaterialTheme.extendedColor.primaryColor,
                )
            }
            SpaceSmall()

            Column(
                modifier = Modifier.weight(1f)
            ) {
                CustomText(
                    text = annualPrise,
                    fontSize = NormalTextSize,
                    color = MaterialTheme.extendedColor.hintColor,
                    lineHeight = SmallTextSize
                )
                CustomText(
                    text = "+${stringResource(Res.string.tanga, subscriptionUi.annual.coin)}",
                    fontSize = SmallTextSize,
                    color = MaterialTheme.extendedColor.primaryColor,
                )
            }
            SpaceSmall()

            Image(
                painter = painterResource(Res.drawable.detail_icon),
                contentDescription = "clock",
                modifier = Modifier.size(NormalIconButtonSize)
            )
        }


    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK,
    ) {
        SubscriptionPaymentItem(
            modifier = Modifier
                .fillMaxWidth(),
            subscriptionUi = SubscriptionUi(
                planId = "10000",
                type = SubscriptionType.PLUS,
                monthly = PlanUi(
                    price = 10000,
                    coin = 10,
                    feature = listOf(),
                    bonus = listOf()
                ),
                annual = PlanUi(
                    price = 100000,
                    coin = 100,
                    feature = listOf(),
                    bonus = listOf()
                )
            ),
            onClick = {}
        )
    }
}