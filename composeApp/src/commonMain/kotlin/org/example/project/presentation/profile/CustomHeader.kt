package org.example.project.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.common.CustomText
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ContainerPadding
import org.example.project.ui.HeaderHeight
import org.example.project.ui.LargeTextSize
import org.example.project.ui.NormalIconButtonPadding
import org.example.project.ui.NormalIconButtonSize
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SpaceMedium
import org.example.project.ui.TextColor
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_left

@Composable
fun CustomHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    fonWeight: FontWeight = FontWeight.W500,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    trailingIcon: (@Composable () -> Unit)? = null,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        shape = RoundedCornerShape(bottomStart = ShapeCornerRadius, bottomEnd = ShapeCornerRadius),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        )
    ) {
        Row(
            modifier = modifier
                .background(BackgroundColor)
                .fillMaxWidth()
                .height(HeaderHeight)
                .padding(horizontal = ContainerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (showBackButton){
                FilledTonalIconButton(
                    modifier = Modifier
                        .size(NormalIconButtonSize),
                    onClick = onBackClick,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = TonalButtonContainerColor,
                        contentColor = TextColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_left),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(NormalIconButtonPadding)
                    )
                }

                SpaceMedium()

            }


            CustomText(
                text = title,
                color = TextColor,
                fontSize = LargeTextSize,
                fontWeight = fonWeight,
                maxLines = 1
            )

            Spacer(modifier = Modifier.weight(1f))

            if (trailingIcon != null) {
                trailingIcon()
            }
        }

    }
}

@Preview
@Composable
private fun PreviewCustomHeader(){
    CustomHeader(
        title = "Shaxsiy ma'lumotlar",
        showBackButton = true
    )
}