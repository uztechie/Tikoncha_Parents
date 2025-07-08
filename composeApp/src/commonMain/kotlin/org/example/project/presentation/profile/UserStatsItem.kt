package org.example.project.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import org.example.project.presentation.common.CustomText
import org.example.project.ui.AppIconInnerPadding
import org.example.project.ui.BackgroundColor
import org.example.project.ui.MainCornerRadius
import org.example.project.ui.NormalIconButtonSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.ProfileStatsContainerHeight
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SmallIconButtonSize
import org.example.project.ui.SmallTextSize
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coin

@Composable
fun UserStatsItem(
    title: String,
    value: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(MainCornerRadius))
            .background(TonalButtonContainerColor)
            .padding(AppIconInnerPadding)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Box(
                modifier = Modifier
                    .size(SmallIconButtonSize)
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.7f)
                )
            }

            SpaceUltraSmall()

            CustomText(
                text = title,
                style = TextStyle(),
                fontSize = SmallTextSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 3
            )

        }

        CustomText(
            text = value,
            color = PrimaryColor,
            fontSize = NormalTextSize,
            fontWeight = FontWeight.SemiBold
        )

    }
}

@Preview
@Composable
private fun Pre(){
    UserStatsItem(
        title = "Tangalaringiz",
        value = "44 ta",
        icon = painterResource(Res.drawable.coin),
        modifier = Modifier.fillMaxWidth().height(ProfileStatsContainerHeight)
    )
}