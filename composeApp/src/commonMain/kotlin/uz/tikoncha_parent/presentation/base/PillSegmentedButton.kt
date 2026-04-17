package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.father_icon
import tikoncha_parents.composeapp.generated.resources.mather_icon
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

// ═════════════════════════════════════════════════════════════
// Data
// ═════════════════════════════════════════════════════════════

/**
 * Segmented button ichidagi bitta item.
 * Icon ixtiyoriy — faqat text bo'lsa null qoldiring.
 */
@Immutable
data class PillSegmentedItem(
    val label: String,
    val icon: Painter? = null,
)

/**
 * Segmented button ranglari. Hamma token lar design system dan keladi,
 * dark/light mode avtomatik.
 */
@Immutable
data class PillSegmentedColors(
    val trackColor: Color,         // tashqi fon (neutral)
    val indicatorColor: Color,     // tanlangan item fon (oq)
    val selectedTextColor: Color,
    val unselectedTextColor: Color,
    val selectedIconColor: Color,
    val unselectedIconColor: Color,
)

object PillSegmentedButtonDefaults {

    /** Figma defaultlari: neutral track, oq indicator, primary text. */
    @Composable
    fun colors(
        trackColor: Color = AppColors.section.secondary,
        indicatorColor: Color = AppColors.modal.primary,
        selectedTextColor: Color = AppColors.text.primary,
        unselectedTextColor: Color = AppColors.text.primary,
        selectedIconColor: Color = AppColors.icon.primary,
        unselectedIconColor: Color = AppColors.icon.primary,
    ): PillSegmentedColors = PillSegmentedColors(
        trackColor = trackColor,
        indicatorColor = indicatorColor,
        selectedTextColor = selectedTextColor,
        unselectedTextColor = unselectedTextColor,
        selectedIconColor = selectedIconColor,
        unselectedIconColor = unselectedIconColor,
    )

    val Height: Dp = 36.dp
    val TrackPadding: Dp = 2.dp
    val ItemGap: Dp = 4.dp
    val IconSize: Dp = 16.dp
    val IconTextGap: Dp = 6.dp
}

// ═════════════════════════════════════════════════════════════
// Component
// ═════════════════════════════════════════════════════════════

/**
 * Pill-shaped segmented button with animated indicator.
 *
 * Figma: Parent-design, node 40002545:17609 (Segmented control)
 * - Track: neutral pill shape
 * - Indicator: pill shape, animated left-right
 * - Text color constant; selection shown by indicator background only
 *
 * @param items tanlov variantlari (2 dan kam bo'lmasin)
 * @param selectedIndex tanlangan element indexi
 * @param onSelected tanlash eventi
 * @param modifier umumiy modifier
 * @param enabled hammasini yoqish/o'chirish
 * @param height component balandligi
 * @param textStyle text uslubi
 * @param colors ranglar
 */
@Composable
fun PillSegmentedButton(
    items: List<PillSegmentedItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = PillSegmentedButtonDefaults.Height,
    textStyle: TextStyle = AppTypography.bodyMdMedium,
    colors: PillSegmentedColors = PillSegmentedButtonDefaults.colors(),
) {
    if (items.size < 2) return

    val density = LocalDensity.current
    val trackPadding = PillSegmentedButtonDefaults.TrackPadding
    val itemGap = PillSegmentedButtonDefaults.ItemGap

    // Track widthni o'lchab itemWidth ni hisoblaymiz
    var itemWidth by remember { mutableStateOf(0.dp) }

    // Outer pill shape (Figma: rounded-100)
    val outerShape: Shape = RoundedCornerShape(percent = 50)
    // Inner indicator shape (Figma: rounded-20)
    val indicatorShape: Shape = RoundedCornerShape(percent = 50)

    Box(
        modifier = modifier
            .height(height)
            .clip(outerShape)
            .background(colors.trackColor)
            .padding(trackPadding)
            .onGloballyPositioned { coords ->
                val totalPx = coords.size.width
                val gapPx = with(density) { (itemGap * (items.size - 1)).toPx() }
                val paddingPx = with(density) { (trackPadding * 2).toPx() }
                val contentPx = totalPx - paddingPx
                val perItemPx = (contentPx - gapPx) / items.size
                itemWidth = with(density) { perItemPx.toDp() }
            },
    ) {
        // ── Sliding indicator ─────────────────
        val indicatorOffset by animateDpAsState(
            targetValue = (itemWidth + itemGap) * selectedIndex,
            label = "PillSegmentedIndicatorOffset",
        )

        if (itemWidth > 0.dp) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(itemWidth)
                    .offset(x = indicatorOffset)
                    .clip(indicatorShape)
                    .background(colors.indicatorColor),
            )
        }

        // ── Items row ─────────────────────────
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(itemGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                PillSegmentedItemContent(
                    item = item,
                    isSelected = isSelected,
                    enabled = enabled,
                    textStyle = textStyle,
                    colors = colors,
                    onClick = { onSelected(index) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
private fun PillSegmentedItemContent(
    item: PillSegmentedItem,
    isSelected: Boolean,
    enabled: Boolean,
    textStyle: TextStyle,
    colors: PillSegmentedColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val textColor = if (isSelected) colors.selectedTextColor else colors.unselectedTextColor
    val iconColor = if (isSelected) colors.selectedIconColor else colors.unselectedIconColor

    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (item.icon != null) {
            Icon(
                painter = item.icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(PillSegmentedButtonDefaults.IconSize),
            )
            Spacer(Modifier.width(PillSegmentedButtonDefaults.IconTextGap))
        }
        Text(
            text = item.label,
            color = textColor,
            style = textStyle,
            maxLines = 1,
        )
    }
}

// ═════════════════════════════════════════════════════════════
// Preview
// ═════════════════════════════════════════════════════════════

@Preview
@Composable
private fun PillSegmentedButtonPreviewLight() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        PillSegmentedButtonPreviewContent()
    }
}

@Preview
@Composable
private fun PillSegmentedButtonPreviewDark() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PillSegmentedButtonPreviewContent()
    }
}

@Composable
private fun PillSegmentedButtonPreviewContent() {
    var selectedText by remember { mutableStateOf(0) }
    var selectedIcon by remember { mutableStateOf(1) }
    var selectedThree by remember { mutableStateOf(0) }

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .background(AppColors.bg.secondary)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Text only — Kunlik / Soatlik
        PillSegmentedButton(
            items = listOf(
                PillSegmentedItem("Kunlik"),
                PillSegmentedItem("Soatlik"),
            ),
            selectedIndex = selectedText,
            onSelected = { selectedText = it },
            modifier = Modifier.fillMaxWidth(),
        )

        // With icons
        PillSegmentedButton(
            items = listOf(
                PillSegmentedItem("Otasi", painterResource(Res.drawable.father_icon)),
                PillSegmentedItem("Onasi", painterResource(Res.drawable.mather_icon)),
            ),
            selectedIndex = selectedIcon,
            onSelected = { selectedIcon = it },
            modifier = Modifier.fillMaxWidth(),
        )

        // 3 items
        PillSegmentedButton(
            items = listOf(
                PillSegmentedItem("Kun"),
                PillSegmentedItem("Hafta"),
                PillSegmentedItem("Oy"),
            ),
            selectedIndex = selectedThree,
            onSelected = { selectedThree = it },
            modifier = Modifier.fillMaxWidth(),
        )

        // Disabled
        PillSegmentedButton(
            items = listOf(
                PillSegmentedItem("Kunlik"),
                PillSegmentedItem("Soatlik"),
            ),
            selectedIndex = 0,
            onSelected = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}