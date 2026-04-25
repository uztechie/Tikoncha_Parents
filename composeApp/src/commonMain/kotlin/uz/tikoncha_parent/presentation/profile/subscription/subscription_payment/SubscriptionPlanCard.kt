package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.price_uzs
import tikoncha_parents.composeapp.generated.resources.price_uzs_per_month
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.presentation.base.CustomRadio
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.ColorWhite
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme


@Composable
fun SubscriptionPlanCard(
    title: String,
    badgeText: String?,
    totalPrice: Int?,
    isSelected: Boolean,
    onClick: (Boolean) -> Unit,
    pricePerMonth: Int,
) {
    val formattedMonthly = stringResource(Res.string.price_uzs_per_month, pricePerMonth.toCurrency(' '))

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            AppColors.border.accentWarning
        } else {
            Color.Transparent
        },
        animationSpec = tween(200),
        label = "border"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 0.dp,
        animationSpec = tween(200),
        label = "border"
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .singleClick { onClick(!isSelected) }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = AppTypography.titleMdSemiBold,
                    color = AppColors.text.inverse,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formattedMonthly,
                    style = AppTypography.titleSmMedium,
                    color = AppColors.text.inverse,
                )
                Space(12.dp)

                CustomRadio(
                    checked = isSelected,
                    onChecked = onClick,
                    checkColor = ColorWhite,
                    unCheckColor = ColorWhite,
                )
            }

            totalPrice?.let { price ->
                Space(14.dp)
                Text(
                    text = stringResource(Res.string.price_uzs, price.toCurrency(' ')),
                    style = AppTypography.titleMdSemiBold,
                    color = AppColors.text.inverse
                )
            }
        }

        if (isSelected) {
            if (badgeText != null) {
                val badgeGradient = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFC9924),
                        Color(0xFFF04438),
                    )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-12).dp)
                        .background(badgeGradient, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.inverse
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            SubscriptionPlanCard(
                title = "Yillik",
                badgeText = "Eng foydali tanlov",
                totalPrice = 199000,
                isSelected = true,
                onClick = {},
                pricePerMonth = 16500
            )
        }
    }
}