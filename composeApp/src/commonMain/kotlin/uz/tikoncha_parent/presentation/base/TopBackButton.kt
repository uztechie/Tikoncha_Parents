package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_back
import uz.tikoncha_parent.ui.theme.AppColors

@Composable
fun TopBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentColor: Color = AppColors.icon.primary,
) {
    Icon(
        painter = painterResource(Res.drawable.arrow_back),
        contentDescription = "Arrow back",
        tint = contentColor,
        modifier = modifier
            .size(24.dp)
            .singleClick { onClick() }
    )
}