package org.example.project.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun CustomHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    fonWeight: FontWeight = FontWeight.W500,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    trailingIcon: (@Composable () -> Unit)? = null,
) {


    val bottomRoundedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = ShapeCornerRadius,
        bottomEnd = ShapeCornerRadius
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = bottomRoundedShape,
                ambientColor = MaterialTheme.colorScheme.primary, // 🌈 Soya rangi shu yerda
                spotColor = MaterialTheme.colorScheme.primary     // Android 12+ uchun
            )
            .padding(bottom = 4.dp),
        shape = bottomRoundedShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
    ) {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .height(HeaderHeight)
                .padding(horizontal = ContainerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (showBackButton) {
                FilledTonalIconButton(
                    modifier = Modifier
                        .size(NormalIconButtonSize),
                    onClick = onBackClick,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onBackground
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
    SpaceSmall()

//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 2.dp),
//        shape = RoundedCornerShape(bottomStart = ShapeCornerRadius, bottomEnd = ShapeCornerRadius),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 1.dp,
//        )
//    )
//    {
//        Row(
//            modifier = modifier
//                .background(BackgroundColor)
//                .fillMaxWidth()
//                .height(HeaderHeight)
//                .padding(horizontal = ContainerPadding),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            if (showBackButton){
//                FilledTonalIconButton(
//                    modifier = Modifier.size(NormalIconButtonSize),
//                    onClick = onBackClick,
//                    colors = IconButtonDefaults.filledTonalIconButtonColors(
//                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                        contentColor = MaterialTheme.colorScheme.onBackground
//                    ),
//                    shape = RoundedCornerShape(10.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(Res.drawable.arrow_left),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(NormalIconButtonPadding)
//                    )
//                }
//
//                SpaceMedium()
//
//            }
//
//            CustomText(
//                text = title,
//                color = TextColor,
//                fontSize = LargeTextSize,
//                fontWeight = fonWeight,
//                maxLines = 1
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//           if (trailingIcon != null) {
//               trailingIcon()
//           }
//        }
//
//    }
}

@Preview
@Composable
private fun Preview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CustomHeader(
            title = "Title",
            showBackButton = true
        )
    }
}