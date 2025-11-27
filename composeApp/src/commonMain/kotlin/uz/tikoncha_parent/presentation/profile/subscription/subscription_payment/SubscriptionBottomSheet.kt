@file:OptIn(ExperimentalMaterial3Api::class)

package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.domain.model.SubscriptionDuration
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.presentation.base.SegmentedToggle
import uz.tikoncha_parent.presentation.profile.subscription.PlanUi
import uz.tikoncha_parent.presentation.profile.subscription.SubscriptionUi
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PriceSubscriptionColor
import uz.tikoncha_parent.ui.PrimaryAlphaColor
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.math.roundToInt

@Composable
fun SubscriptionBottomSheet(
    visible: Boolean,
    subscription: SubscriptionUi,
    hasSubscription: Boolean = false,
    onDismiss: () -> Unit,
    onByClick: (price: Int, tier: SubscriptionDuration, planId: String) -> Unit
) {
    if (!visible) return

    val options = listOf(
        stringResource(Res.string.oylik) to null,
        stringResource(Res.string.yillik) to null
    )
    var selectedOptionIndex by remember {
        mutableIntStateOf(0)
    }


    val sheetState = rememberModalBottomSheetState(true)


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {},
        shape = RoundedCornerShape(topStart = CardCornerRadius, topEnd = CardCornerRadius),
        containerColor = MaterialTheme.extendedColor.cardColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ContainerPadding)
        )
        {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(
                        MaterialTheme.extendedColor.hintColor, RoundedCornerShape(
                            CardCornerRadius
                        )
                    )
                    .padding(vertical = 4.dp, horizontal = 26.dp)
            )
            SpaceMedium()
            Image(
                painter = when (subscription.type) {
                    SubscriptionType.FREE -> painterResource(Res.drawable.tikoncha_plus)
                    SubscriptionType.PLUS -> painterResource(Res.drawable.tikoncha_plus)
                    SubscriptionType.PRO -> painterResource(Res.drawable.tikoncha_pro)
                },
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(0.5f)
            )

            SpaceMedium()
            HorizontalDivider(
                color = MaterialTheme.extendedColor.hintColor.copy(0.4f),
            )
            SpaceMedium()

            SegmentedToggle(
                options = options,
                fontSize = NormalTextSize,
                selectedIndex = selectedOptionIndex,
                onOptionSelected = {
                    selectedOptionIndex = it
                }
            )
            SpaceMedium()


            val priceText = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.extendedColor.primaryColor,
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    val (price, title) = if (selectedOptionIndex == 0) {
                        subscription.monthly.price.toCurrency() to stringResource(Res.string.som)
                    } else {
                        subscription.annual.price.toCurrency() to stringResource(Res.string.som)
                    }
                    append(price)
                    append(" ")
                    append(title)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = priceText,
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                if (selectedOptionIndex == 0){
                    val monthlyPrice = subscription.monthly.price
                    val annualPrice = subscription.annual.price
                    val yearlyPrice = monthlyPrice * 12

                    val profitPrice = if (yearlyPrice > 0 && yearlyPrice > annualPrice){
                        val saved = yearlyPrice - annualPrice
                        ((saved.toDouble() / yearlyPrice.toDouble()) * 100).roundToInt()
                    } else {
                        0
                    }

                    val compareText = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = NormalTextSize,
                                color = OnPrimaryColor,
                            )
                        ) {
                            val (price, title) = if (selectedOptionIndex == 0) {
                                subscription.annual.price.toCurrency() to stringResource(Res.string.yillik)
                            } else {
                                null to ""
                            }
                            append(price)
                            append(" /")
                            append(title)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .background(PriceSubscriptionColor, RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp)
                    ){
                        CustomText(
                            text = compareText,
                            fontSize = NormalTextSize,
                            fontWeight = FontWeight.W500,
                            color = OnPrimaryColor
                        )
                    }
                    SpaceUltraSmall()
                    Box(
                        modifier = Modifier.background(PrimaryAlphaColor, RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp)
                    ){
                        CustomText(
                            text = "${stringResource(Res.string.foyda)} - $profitPrice%",
                            fontSize = NormalTextSize,
                            fontWeight = FontWeight.W500,
                            color = OnPrimaryColor
                        )
                    }
                }
            }

            SpaceMedium()

            CustomText(
                text = stringResource(Res.string.imkoniyatlar),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )

            val plan = if (selectedOptionIndex == 0)
                subscription.monthly
            else
                subscription.annual

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                plan.feature.forEach { data ->
                    SubscriptionFeatureSectionsItem(
                        text = data
                    )
                }
            }

            CustomText(
                text = stringResource(Res.string.obunaga_sovga_sifatida),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                plan.bonus.forEach { data ->
                    SubscriptionFeatureSectionsItem(
                        text = data
                    )
                }
            }

            SpaceLarge()

            CustomButton(
                enabled = !hasSubscription,
                text = if (hasSubscription) stringResource(Res.string.obuna_faollashtirilgan)
                else stringResource(Res.string.sotib_olish),
                onClick = {
                    onByClick(
                        plan.price,
                        if (selectedOptionIndex == 0)
                            SubscriptionDuration.MONTHLY
                        else
                            SubscriptionDuration.ANNUAL,
                        subscription.planId
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ){
        SubscriptionBottomSheet(
            visible = true,
            subscription = SubscriptionUi(
                planId = "1",
                type = SubscriptionType.FREE,
                monthly = PlanUi(
                    price = 1000,
                    coin = 10,
                    feature = listOf(),
                    bonus = listOf()
                ),
                annual = PlanUi(
                    price = 10000,
                    coin = 10,
                    feature = listOf(),
                    bonus = listOf()
                )
            ),
            onDismiss = {},
            onByClick = { _,_, _ ->},
            hasSubscription = true
        )
    }
}