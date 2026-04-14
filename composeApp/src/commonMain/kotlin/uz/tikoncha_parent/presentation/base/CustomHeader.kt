package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
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
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    trailingIcon: (@Composable () -> Unit)? = null,
) {


    val bottomRoundedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = ShapeCornerRadius,
        bottomEnd = ShapeCornerRadius
    )

    Row(
        modifier = modifier
            .zIndex(1f)
            .fillMaxWidth()
            .height(HeaderHeight)
            .topShadow(
                shape = RoundedCornerShape(ShapeCornerRadius),
                color = AppColors.bg.page
            )
            .background(
                color = AppColors.bg.page,
                shape = bottomRoundedShape
            )
            .padding(horizontal = ContainerPadding),
        verticalAlignment = Alignment.CenterVertically
    )
    {

        if (showBackButton) {
            FilledTonalIconButton(
                modifier = Modifier
                    .size(NormalIconButtonSize),
                onClick = onBackClick,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.extendedColor.cardColor,
                    contentColor = MaterialTheme.extendedColor.onBackgroundColor
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_left),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(NormalIconButtonPadding)
                )
            }
            SpaceMedium()
        }

        Text(
            text = title,
            style = AppTypography.headlineSmSemiBold,
            color = AppColors.text.primary,
            maxLines = 1,
        )
        SpaceMedium()
        Spacer(modifier = Modifier.weight(1f))

        if (trailingIcon != null) {
            trailingIcon()
        }
    }

}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
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