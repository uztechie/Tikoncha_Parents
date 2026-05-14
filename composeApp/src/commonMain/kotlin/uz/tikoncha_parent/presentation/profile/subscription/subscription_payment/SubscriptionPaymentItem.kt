package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*

@Composable
fun SubscriptionPaymentItem(
    modifier: Modifier = Modifier,
    subscriptionUi: SubscriptionUi,
    imagePay: SubscriptionType = SubscriptionType.FREE,
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }

    val titleIcon = when (imagePay) {
        SubscriptionType.FREE -> painterResource(Res.drawable.tikoncha_plus)
        SubscriptionType.PLUS -> painterResource(Res.drawable.tikoncha_plus)
        SubscriptionType.PRO -> painterResource(Res.drawable.tikoncha_pro)
    }

//    val monthlyPrise = buildAnnotatedString {
//        append(stringResource(Res.string.oylik))
//        append("/ ")
//        withStyle(
//            style = SpanStyle(
//                color = MaterialTheme.extendedColor.primaryColor,
//                fontWeight = FontWeight.W600,
//                fontSize = NormalTextSize
//            )
//        ) {
//            append("${subscriptionUi.monthly.price.toCurrency()}")
//        }
//    }
//
//    val annualPrise = buildAnnotatedString {
//        append(stringResource(Res.string.yillik))
//        append("/ ")
//        withStyle(
//            style = SpanStyle(
//                color = MaterialTheme.extendedColor.primaryColor,
//                fontWeight = FontWeight.W600,
//                fontSize = NormalTextSize
//            )
//        ) {
//            append("${subscriptionUi.annual.price.toCurrency()}")
//        }
//    }

    val monthlyPriceText = remember(subscriptionUi.monthly.price) {
        subscriptionUi.monthly.price.toCurrency()
    }
    val annualPriceText = remember(subscriptionUi.annual.price) {
        subscriptionUi.annual.price.toCurrency()
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CardCornerRadius))
            .background(MaterialTheme.extendedColor.cardColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // Karta eniga qarab responsive spacing/sizes
        val isCompact = maxWidth < 360.dp
        val gap = if (isCompact) ContainerPadding / 2 else ContainerPadding
        val iconMaxWidth = if (isCompact) 160.dp else 220.dp

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
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryColor),
            )

            SpaceSmall()
            DividerHorizontal(
                modifier = Modifier
                    .padding(vertical = ContainerPadding / 2)
            )
            SpaceSmall()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                PriceBlock(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.oylik),
                    priceText = monthlyPriceText,
                    coin = subscriptionUi.monthly.coin
                )

                PriceBlock(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.yillik),
                    priceText = annualPriceText,
                    coin = subscriptionUi.annual.coin
                )

//                Image(
//                    painter = painterResource(Res.drawable.detail_icon),
//                    contentDescription = "details",
//                    modifier = Modifier
//                        .size(NormalIconButtonSize)
//                        .clip(CircleShape),
//                    colorFilter = ColorFilter.tint(PrimaryColor, blendMode = BlendMode.Color)
//                )
            }
        }
    }
}

@Composable
private fun PriceBlock(
    modifier: Modifier = Modifier,
    label: String,
    priceText: String,
    coin: Int
) {
    val color = MaterialTheme.extendedColor.primaryColor
    val fonSize = NormalTextSize

    Column(modifier = modifier) {
        val priceAnnotated = remember(label, priceText) {
            buildAnnotatedString {
                append(label)
                append("/ ")
                withStyle(
                    SpanStyle(
                        color = color,
                        fontWeight = FontWeight.W600,
                        fontSize = fonSize
                    )
                ) {
                    append(priceText)
                }
            }
        }

        CustomText(
            text = priceAnnotated,
            fontSize = NormalTextSize,
            color = MaterialTheme.extendedColor.hintColor,
            lineHeight = SmallTextSize
        )

        CustomText(
            text = "+${stringResource(Res.string.tanga, coin)}",
            fontSize = SmallTextSize,
            color = MaterialTheme.extendedColor.primaryColor,
        )
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