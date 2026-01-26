package uz.tikoncha_parent.presentation.profile.subscription.payment

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.*
import uz.tikoncha_parent.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun PaymentOption(
    modifier: Modifier = Modifier,
    paymentType: PaymentType,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(72.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(TextFieldCornerRadius))
            .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .padding(horizontal = 35.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (paymentType.icon != null){
                Image(
                    painter = painterResource(paymentType.icon),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                )
            }
            else{
                CustomText(
                    text = paymentType.title?:"",
                    color = MaterialTheme.extendedColor.onBackgroundColor,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PrimaryColor
                )
            )
        }
    }
}



@Composable
@Preview
private fun Preview() {
    TikonchaParentTheme(mode = ThemeMode.DARK){
        PaymentOption(
            onClick = {},
            paymentType = PaymentType.AppStore,
            isSelected = false
        )
    }

}
