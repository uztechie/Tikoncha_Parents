package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    fonWeight: FontWeight = FontWeight.W500,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    trailingIcon: (@Composable () -> Unit)? = null,
) {



    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {

        if (showBackButton) {
            Space(4.dp)
            IconButton(
                modifier = Modifier
                    .size(36.dp),
                onClick = onBackClick,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = AppColors.icon.primary
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = "",
                    tint = AppColors.icon.primary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
        else{
            Space(16.dp)
        }

        Text(
            text = title,
            style = AppTypography.headlineSmSemiBold,
            color = AppColors.text.primary,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
        )
        Space(16.dp)

        if (trailingIcon != null) {
            trailingIcon()
//            Space(16.dp)
        }
    }

}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CustomHeader(
                title = "Title",
                showBackButton = true
            )
        }
    }
}