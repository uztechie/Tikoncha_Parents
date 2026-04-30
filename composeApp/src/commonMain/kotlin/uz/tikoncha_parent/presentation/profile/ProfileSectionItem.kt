package uz.tikoncha_parent.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun ProfileSectionItem(
    icon: Painter,
    title: String,
    onItemClick: () -> Unit,
    divider: Boolean = true,
    textColor: Color = AppColors.text.primary,
    iconColor: Color = AppColors.icon.accentPrimary
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(
                enabled = true,
                onClick = { onItemClick() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier
                    .padding(start = 7.dp)
                    .size(SmallIconSize)
            )
            Spacer(Modifier.width(19.dp))

            Text(
                text = title,
                color = textColor,
                style = AppTypography.titleSmMedium
            )
        }

        if (divider) {
            HorizontalDivider(
                color = AppColors.border.secondarySubtle,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        ProfileSectionItem(
            icon = painterResource(Res.drawable.profile),
            title = "Shaxsiy",
            onItemClick = {}
        )
    }
}