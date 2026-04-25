package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.layers_sub
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun SubscriptionFeatureItem(
    iconRes: DrawableResource,
    title: String,
    iconTint: Color = AppColors.icon.accentPrimary,
    description: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(32.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = AppTypography.emphasizedLgMedium,
                color = AppColors.text.inverse,
            )
            Space(4.dp)

            Text(
                text = description,
                style = AppTypography.emphasizedSmMedium,
                color = AppColors.text.inverse,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme (
        ThemeMode.DARK
    ) {
        SubscriptionFeatureItem(
            iconRes = Res.drawable.layers_sub,
            title = "",
            iconTint = Color.Black,
            description = "",
        )
    }
}