package org.example.project.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.example.project.presentation.base.theme.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun CustomSelectionButton(
    modifier: Modifier = Modifier,
    text: String,
    painter: Painter,
    onClick: () -> Unit,
    loading: Boolean = false,
    label: String = "",
    showTrailingIcon: Boolean = true
) {

    val color = if (text.isEmpty()) HintTextColor else TextColor
    val newText = text.ifEmpty { label }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(TextFieldCornerRadius))
            .fillMaxWidth()
            .border(1.dp, PrimaryColor, RoundedCornerShape(TextFieldCornerRadius))
            .background(BackgroundColor)
            .padding(horizontal = TextFieldInnerPadding)
            .height(TextFieldHeight)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    if (!loading) {
                        onClick()
                    }
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .size(20.dp),
            tint = PrimaryColor
        )

        Spacer(Modifier.width(TextFieldInnerPadding))

        CustomText(
            text = newText,
            fontSize = NormalTextSize,
            color = color,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
        )

        if (showTrailingIcon) {

            SpaceSmall()

            if (loading) {
                CircularProgressIndicator(
                    color = PrimaryColor,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    painter = painterResource(Res.drawable.arrow_right),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer {
                            rotationZ = 90f
                        },
                    tint = TextColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    CustomSelectionButton(
        text = "",
        painter = painterResource(Res.drawable.lock),
        onClick = {},
        label = "Viloyat"
    )
}