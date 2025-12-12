package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.common.Util.toCurrency
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CoinPackItem(
    coins: Int,
    price: Long,
    onClick: () -> Unit,
    discountPercent: Int? = 30,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CardCornerRadius))
            .clickable(onClick = onClick)
            .background(
                MaterialTheme.extendedColor.cardColor,
                RoundedCornerShape(CardCornerRadius)
            )
            .padding(16.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(SmallIconButtonSize)
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(MaterialTheme.extendedColor.backgroundColor),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(Res.drawable.coin),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.7f)
                )
            }

            SpaceSmall()
            Column {
                CustomText(
                    text = stringResource(Res.string.tangachalar),
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.extendedColor.hintColor
                )
                CustomText(
                    text = "${coins.toCurrency()} ${stringResource(Res.string.tangachalar)}"
                )
            }

            Spacer(Modifier.weight(1f))

            CustomText(
                text = "${price.toCurrency()} UZS",
                fontSize = NormalTextSize,
                color = PrimaryColor,
                fontWeight = FontWeight.W600
            )
        }
        SpaceMedium()
        DividerHorizontal()
        SpaceMedium()

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.extendedColor.primaryColor,
                        RoundedCornerShape(TextFieldCornerRadius))
                    .padding(horizontal = 6.dp)
            ){
                CustomText(
                    text = "${discountPercent?:0}%",
                    fontSize = NormalTextSize,
                    fontWeight = FontWeight.SemiBold,
                    color = OnPrimaryColor,
                )
            }
            SpaceSmall()
            CustomText(
                text = stringResource(Res.string.chegirma),
                color = MaterialTheme.extendedColor.hintColor,
                fontSize = NormalTextSize,
                modifier = Modifier.weight(1f)
            )

            Image(
                painter = painterResource(Res.drawable.arrow_pay),
                contentDescription = null,
                modifier = Modifier
                    .size(LargeIconSize)
                    .background(PrimaryColor, CircleShape)
                    .padding(9.dp)
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
        CoinPackItem(
            coins = 5000,
            price = 350000,
            onClick = {}
        )
    }
}