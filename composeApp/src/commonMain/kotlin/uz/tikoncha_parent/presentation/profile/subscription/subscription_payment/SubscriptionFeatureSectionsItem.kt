package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import org.jetbrains.compose.resources.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.*

@Composable
fun SubscriptionFeatureSectionsItem(
    text: String
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColor.cardColor),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.coins),
                contentDescription = "",
                modifier = Modifier.size(SmallIconSize),
                colorFilter = ColorFilter.tint(PrimaryColor)
            )

            SpaceSmall()
            CustomText(
                text = text,
                color = MaterialTheme.extendedColor.textColor,
                fontSize = NormalTextSize,
            )
        }
    }
}

@Preview()
@Composable
private fun Pre() {
    SubscriptionFeatureSectionsItem(
       text = "Salom"
    )
}