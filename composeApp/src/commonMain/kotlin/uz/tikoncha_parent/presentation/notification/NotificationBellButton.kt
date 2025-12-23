package uz.tikoncha_parent.presentation.notification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.notification
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.DisableButtonColor
import uz.tikoncha_parent.ui.LargeIconButtonSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.TextColor
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun NotificationBellButton(
    count: Int,
    onClick: ()-> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        FilledTonalIconButton(
            modifier = modifier
                .size(LargeIconButtonSize)
                .clip(CircleShape),
            onClick = onClick,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.extendedColor.cardColor,
                contentColor = MaterialTheme.extendedColor.textColor
            ),
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(Res.drawable.notification),
                contentDescription = "Notification",
                tint = PrimaryColor,
            )
        }
        if (count > 0){
            Badge(
                modifier = Modifier.align(Alignment.TopEnd),
                containerColor = DisableButtonColor,
                contentColor = TextColor
            ) {
                CustomText(
                    text = if (count > 99) "99+" else count.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W600),
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NotificationBellButton(
        count = 5,
        onClick = {}
    )
}