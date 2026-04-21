package uz.tikoncha_parent.presentation.policy.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.theme.AppTypography


/**
 * Segmented Tab Bar (Figma spec: Items, 44×auto).
 *
 * Har bir tab pastda 2dp border'ga ega:
 *  - tanlangan tab: border #C3955B (sariq)
 *  - tanlanmagan tab: border #EFEFF0 (ochiq kul)
 *
 * Tanlangan border tanlov o'zgarganda silliq siljiydi (animatsiya).
 *
 * Ishlatish:
 * ```
 * var selected by remember { mutableStateOf(0) }
 * SegmentedTabBar(
 *     items = listOf("Ilovalar", "Saytlar"),
 *     selectedIndex = selected,
 *     onSelect = { selected = it }
 * )
 * ```
 *
 * Default qiymatlar Figma spec:
 * - height 44dp
 * - tab padding 10dp
 * - border qalinligi 2dp (ikkala holatda ham)
 * - font 13sp, Medium, lineHeight 14
 * - tanlangan rang #C3955B, tanlanmagan #EFEFF0
 * - text rang #22262F (barcha tab uchun bir xil)
 */
@Composable
fun SegmentedTabBar(
    items: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: SegmentedTabBarColors = SegmentedTabBarDefaults.colors(),
    height: Dp = 44.dp,
    tabPadding: Dp = 10.dp,
    borderWidth: Dp = 2.dp,
    animationDurationMs: Int = 250,
    textStyle: TextStyle = AppTypography.bodyLgMedium
) {
    require(items.isNotEmpty()) { "items bo'sh bo'lmasligi kerak" }
    val safeIndex = selectedIndex.coerceIn(0, items.lastIndex)

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val tabCount = items.size
        val tabWidth = maxWidth / tabCount

        // Tanlangan indicator offsetini animatsiya qilish
        val indicatorOffset = remember { Animatable(safeIndex.toFloat()) }
        LaunchedEffect(safeIndex) {
            indicatorOffset.animateTo(
                targetValue = safeIndex.toFloat(),
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    easing = FastOutSlowInEasing
                )
            )
        }

        Box(modifier = Modifier.fillMaxWidth().height(height)) {
            // 1) Butun kenglik bo'ylab disabled border chizig'i (tagida)
            //    Bu tanlanmagan tablarning ostidagi chiziq bo'lib xizmat qiladi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(borderWidth)
                    .background(colors.unselectedBorderColor)
                    .align(Alignment.BottomStart)
            )

            // 2) Tanlangan tab ostidagi sariq border — animatsiya bilan siljiydi
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = tabWidth * indicatorOffset.value)
                    .width(tabWidth)
                    .height(borderWidth)
                    .background(colors.selectedBorderColor)
            )

            // 3) Tab matnlari — ustida
            Row(modifier = Modifier.fillMaxWidth().height(height)) {
                items.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .height(height)
                            .clickable { onSelect(index) }
                            .padding(tabPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = colors.textColor,
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}


// =====================================================================
//  COLORS
// =====================================================================

@Immutable
data class SegmentedTabBarColors(
    val textColor: Color,
    val selectedBorderColor: Color,
    val unselectedBorderColor: Color
)

object SegmentedTabBarDefaults {

    @Composable
    fun colors(
        textColor: Color = Color(0xFF22262F),               // text/primary
        selectedBorderColor: Color = Color(0xFFC3955B),     // border/accent-emphasis
        unselectedBorderColor: Color = Color(0xFFEFEFF0)    // border/disabled
    ): SegmentedTabBarColors = SegmentedTabBarColors(
        textColor = textColor,
        selectedBorderColor = selectedBorderColor,
        unselectedBorderColor = unselectedBorderColor
    )
}


// =====================================================================
//  PREVIEW
// =====================================================================

@Preview
@Composable
private fun SegmentedTabBarPreview_Default() {
    var selected by remember { mutableStateOf(0) }

    PreviewSurfaceTab {
        SegmentedTabBar(
            items = listOf("Ilovalar", "Saytlar"),
            selectedIndex = selected,
            onSelect = { selected = it },
        )
    }
}

@Preview
@Composable
private fun SegmentedTabBarPreview_Saytlar() {
    var selected by remember { mutableStateOf(1) }

    PreviewSurfaceTab {
        SegmentedTabBar(
            items = listOf("Ilovalar", "Saytlar"),
            selectedIndex = selected,
            onSelect = { selected = it },
        )
    }
}

@Preview
@Composable
private fun SegmentedTabBarPreview_Three() {
    var selected by remember { mutableStateOf(1) }

    PreviewSurfaceTab {
        SegmentedTabBar(
            items = listOf("Hammasi", "Faol", "Tugallangan"),
            selectedIndex = selected,
            onSelect = { selected = it },
        )
    }
}

@Preview
@Composable
private fun SegmentedTabBarPreview_CustomColors() {
    var selected by remember { mutableStateOf(0) }

    PreviewSurfaceTab {
        SegmentedTabBar(
            items = listOf("Kunlik", "Haftalik", "Oylik"),
            selectedIndex = selected,
            onSelect = { selected = it },
            colors = SegmentedTabBarDefaults.colors(
                selectedBorderColor = Color(0xFF7B61FF)
            ),
        )
    }
}

@Composable
private fun PreviewSurfaceTab(
    bg: Color = Color(0xFFF9F3E9),
    content: @Composable () -> Unit
) {
    Surface(color = bg) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content()
        }
    }
}