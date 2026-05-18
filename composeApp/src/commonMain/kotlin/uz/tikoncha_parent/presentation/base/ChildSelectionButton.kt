package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_down_reg
import tikoncha_parents.composeapp.generated.resources.arrows_vertical_expand
import tikoncha_parents.composeapp.generated.resources.plus_symbol
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
import uz.tikoncha_parent.ui.ColorWhite
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme


@Composable
fun ChildSelectionButton(
    modifier: Modifier = Modifier,
    text: String,
    label: String = "",
    imageUrl: String = "",
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    trailingIcon: Boolean = true,
    background: Color = AppColors.section.tertiary,
    textStyle: TextStyle = AppTypography.titleSmMedium,
) {
    val color = if (text.isEmpty()) AppColors.text.secondary else AppColors.text.primary
    val newText = text.ifEmpty { label }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val isCompact = maxWidth < 360.dp
        val buttonHeight =
            if (isCompact) DialogButtonHeight.coerceAtMost(48.dp) else DialogButtonHeight
        val avatarSize = if (isCompact) 28.dp else 36.dp
        val paddingH = if (isCompact) 10.dp else 12.dp

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .clip(shape)
                .background(background, shape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onClick()
                    }
                )
                .padding(start = 4.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, AppColors.bg.surface, CircleShape)
                    .background(AppColors.bg.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when {
                    text.isEmpty() -> {
                        Icon(
                            painter = painterResource(Res.drawable.plus_symbol),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp),
                            tint = AppColors.icon.accentPrimary
                        )
                    }

                    imageUrl.isNotEmpty() -> {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "",
                            error = painterResource(Res.drawable.profile_hedgehog_img),
                            placeholder = painterResource(Res.drawable.profile_hedgehog_img),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(Res.drawable.profile_hedgehog_img),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
            }
            SpaceSmall()

            Text(
                text = newText,
                style = textStyle,
                color = color,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(Modifier.width(4.dp))

            if (trailingIcon) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_down_reg),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = color,
                )
                Spacer(Modifier.width(4.dp))
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorWhite)
                .padding(vertical = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ChildSelectionButton(
                text = "",
                onClick = {},
                label = "Viloyat"
            )
        }
    }
}