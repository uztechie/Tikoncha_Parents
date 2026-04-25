package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun CustomRadio(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
    unCheckColor: Color = AppColors.icon.secondary,
    checkColor: Color = AppColors.icon.accentPrimary,
) {

    val color = if (checked) checkColor else unCheckColor

    Box(
        modifier = modifier
            .size(20.dp)
            .background(Color.Transparent, CircleShape)
            .border(2.dp, color, CircleShape)
            .singleClick {
                onChecked(!checked)
            },
        contentAlignment = Alignment.Center
    ){
        AnimatedVisibility(
            visible = checked
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape),
                contentAlignment = Alignment.Center
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun PRe() {
    TikonchaParentTheme {
        CustomRadio(
            checked = true,
            onChecked = {}
        )
    }
}

