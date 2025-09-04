package uz.tikoncha_parent.presentation.profile

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import uz.tikoncha_parent.ui.AppIconInnerPadding
import uz.tikoncha_parent.ui.MainCornerRadius
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ProfileStatsContainerHeight
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coin
import uz.saidburxon.newedu.presentation.base.CustomText

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
            .background(MaterialTheme.colorScheme.tertiaryContainer)
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
                    .background(MaterialTheme.colorScheme.background),
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