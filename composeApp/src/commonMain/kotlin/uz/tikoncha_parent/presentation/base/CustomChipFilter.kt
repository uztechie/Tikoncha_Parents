package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.tikoncha_parent.ui.theme.extendedColor

/**
 * Ixcham chip: minimal padding, 2-3 harfli label, tanlanganda yashil.
 * KMP-friendly: Material3 + ripple bor, contentPaddingni o'zimiz boshqaramiz.
 */

@Composable
fun CustomChipFilter(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // O'lchamlar
    height: Dp = 36.dp,
    minWidth: Dp = 42.dp,            // 2 harf sig'ishi uchun yetarli
    horizontalPadding: Dp = 6.dp,    // ichki horizontal padding (label atrofida)
    cornerRadius: Dp = 8.dp,         // yumaloqlik
    // Ranglar
    selectedContainerColor: Color = MaterialTheme.extendedColor.primaryColor,
    unselectedContainerColor: Color = MaterialTheme.extendedColor.cardColor,
    selectedLabelColor: Color = Color.White,
    unselectedLabelColor: Color = MaterialTheme.extendedColor.textColor,
    // Matn stili
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.W400,
) {
    val shape = RoundedCornerShape(cornerRadius)
    val disabledContainerColor = MaterialTheme.extendedColor.disabledBgColor
    val disabledContentColor = MaterialTheme.extendedColor.disabledContentColor

    val bg = animateColorAsState(
        targetValue = if (selected) selectedContainerColor else if (!enabled) disabledContainerColor else unselectedContainerColor,
        label = "chip-bg"
    )
    val fg = animateColorAsState(
        targetValue = if (selected) selectedLabelColor else if (!enabled) disabledContentColor else unselectedLabelColor,
        label = "chip-fg"
    )

    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = minWidth, minHeight = height)
            .sizeIn(minHeight = height) // balandlikni kafolatlaydi
            .clip(shape)
            .background(bg.value, shape)
            .clickable(
                interactionSource = interaction,
                indication = LocalIndication.current,
                onClick = {
                    if (enabled) onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = fg.value,
            fontSize = fontSize,
            fontWeight = fontWeight,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier.padding(horizontal = horizontalPadding) // 🔹 ichki padding
        )
    }
}