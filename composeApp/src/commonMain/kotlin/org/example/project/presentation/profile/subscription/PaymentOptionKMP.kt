package org.example.project.presentation.profile.subscription

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.example.project.presentation.common.CustomText
import org.example.project.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.pay_me

@Composable
fun PaymentOptionKMP(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(Res.drawable.pay_me),
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(72.dp)
            .border(1.dp, HintTextColor, RoundedCornerShape(ContainerCornerRadius))
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
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = TextColor
                )
            )
        }
    }
}



@Composable
@Preview
private fun Preview() {
    PaymentOptionKMP(
        onClick = {},
        isSelected = true
    )
}
