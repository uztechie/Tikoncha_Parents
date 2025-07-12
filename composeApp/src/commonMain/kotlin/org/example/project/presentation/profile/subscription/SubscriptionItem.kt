package org.example.project.presentation.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.ContainerPadding
import org.example.project.ui.HintTextColor
import org.example.project.ui.LanguageTonalIconColor
import org.example.project.ui.NormalIconButtonPadding
import org.example.project.ui.NormalLargeTextSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.SmallTextSize
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceSmall
import org.example.project.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ai_icon
import tikoncha_parents.composeapp.generated.resources.icon_coins

@Composable
fun SubscriptionItem(
    selectedSubscription: SubscriptionType,
    onSubscriptionSelected: (SubscriptionType) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ContainerPadding),
    ) {

        SubscriptionType.values().forEach { subscription ->

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) { onSubscriptionSelected(subscription) }
            ) {

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            LanguageTonalIconColor,
                            RoundedCornerShape(NormalIconButtonPadding)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(subscription.iconId),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }

                SpaceLarge()

                Column {

                    Text(
                        text = subscription.settingName,
                        fontSize = NormalLargeTextSize,
                        fontWeight = FontWeight.Medium,
                        style = TextStyle()
                    )

                    SpaceUltraSmall()

                    Text(
                        text = subscription.subtitle,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.Medium,
                        color = HintTextColor,
                        style = TextStyle()
                    )
                }
            }
        }
    }
}

enum class SubscriptionType(
    val iconId: DrawableResource,
    val settingName: String,
    val subtitle: String,

    ) {
    COIN(
        iconId = Res.drawable.icon_coins,
        settingName = "Tangachalar",
        subtitle = "Har oy 500 dan ortiq tangalar"
    ),
    AI(
        iconId = Res.drawable.ai_icon,
        settingName = "Suniy intelekt",
        subtitle = "AI yordamida yangi imkoniyatlar"
    ),
    COIN_1(
        iconId = Res.drawable.icon_coins,
        settingName = "Tangachalar",
        subtitle = "Har oy 500 dan ortiq tangalar"
    ),
    AI_1(
        iconId = Res.drawable.ai_icon,
        settingName = "Suniy intelekt",
        subtitle = "AI yordamida yangi imkoniyatlar"
    )
}