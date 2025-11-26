package uz.tikoncha_parent.presentation.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.jetbrains.compose.resources.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*

@Composable
fun SubscriptionSectionsItem(
    icon: Painter,
    section: SubscriptionTypeSection,
    onItemClick: (section: SubscriptionTypeSection) -> Unit
) {

    val title = when (section) {
        SubscriptionTypeSection.PREMIUM -> stringResource(Res.string.premium)
        SubscriptionTypeSection.DONATION -> stringResource(Res.string.sovga_qilish)
        SubscriptionTypeSection.INVITING_FRIENDS -> stringResource(Res.string.dostlarni_taklif_qilish)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.backgroundColor)
            .clickable(
                indication = null,
                interactionSource = null,
                enabled = true,
                onClick = { onItemClick(section) }
            ),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.size(NormalIconButtonSize)
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(MaterialTheme.extendedColor.cardColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                )
            }

            SpaceSmall()

            CustomText(
                text = title,
                color = MaterialTheme.extendedColor.textColor,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )
        }
        DividerHorizontal()
    }
}

@Preview()
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        SubscriptionSectionsItem(
            icon = painterResource(Res.drawable.profile),
            section = SubscriptionTypeSection.DONATION,
            onItemClick = {

            }
        )
    }
}