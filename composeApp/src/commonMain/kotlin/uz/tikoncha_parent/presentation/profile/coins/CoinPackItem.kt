package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun CoinPackageItem(
    modifier: Modifier = Modifier,
    hasBorder: Boolean = false,
    coinPackageUi: CoinPackageUi
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .simpleShadow(shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (hasBorder) {
                    Modifier.border(2.dp, AppColors.border.accentEmphasis, RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            )
            .background(AppColors.bg.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Yuqori qism: Icon + Tanga miqdori
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Coin icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            AppColors.bg.accentWarningContainer,
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.coin),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Tanga miqdori — katta, asosiy element
                Text(
                    text = stringResource(Res.string.tanga_s, coinPackageUi.coins.toCurrency()),
                    color = AppColors.text.secondary,
                    style = AppTypography.titleLgMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Divider
            HorizontalDivider(
                thickness = 1.dp,
                color = AppColors.border.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pastki qism: Chegirmali narx (chap) + Eski narx (o'ng)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = coinPackageUi.priceInString,
                    color = AppColors.text.label,
                    style = AppTypography.bodyMdMedium,
                    textDecoration = TextDecoration.LineThrough,
                    maxLines = 1
                )
                Text(
                    text = coinPackageUi.priceWithDiscountInString,
                    color = AppColors.text.accentEmphasis,
                    style = AppTypography.titleMdMedium,
                    maxLines = 1
                )


            }
        }

        // Badge — yuqori o'ng burchakda, karta burchagiga yopishgan
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFC9924), Color(0xFFF04438))
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 16.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 0.dp
                    )
                )
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-${coinPackageUi.discountPercent}%",
                color = AppColors.text.inverse,
                style = AppTypography.bodyMdSemiBold
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCoinsItem() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        CoinPackageItem(
            coinPackageUi = CoinPackageUi(
                coins = 1000,
                priceInString = "100,000 UZS",
                discountPercent = 25,
                priceWithDiscountInString = "80,000 UZS",
                price = 100000,
                priceWithDiscount = 80000,
                discountedPrice = 20000
            ),
        )
    }
}