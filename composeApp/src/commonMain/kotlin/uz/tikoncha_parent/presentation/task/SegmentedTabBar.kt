package uz.tikoncha_parent.presentation.task

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.policy.common.SegmentedTabBarColors
import uz.tikoncha_parent.presentation.policy.common.SegmentedTabBarDefaults
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun SegmentedTabBar(
    items: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: SegmentedTabBarColors = SegmentedTabBarDefaults.colors(),
    height: Dp = 44.dp,
    tabPadding: Dp = 16.dp,
    borderWidth: Dp = 2.dp,
    animationDurationMs: Int = 250,
    textStyle: TextStyle = AppTypography.bodyLgMedium,
    fadeColor: Color = Color(0xFFF9F3E9),     // YANGI: fon rangi (ota-component bilan bir xil)
    fadeWidth: Dp = 32.dp,                    // YANGI: gradient kengligi
    autoScrollToSelected: Boolean = true      // YANGI: tanlanganga avto-scroll
) {
    require(items.isNotEmpty()) { "items bo'sh bo'lmasligi kerak" }
    val safeIndex = selectedIndex.coerceIn(0, items.lastIndex)
    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    // Har bir tabning eni va x-pozitsiyasini yodda saqlaymiz
    val tabWidths = remember(items.size) {
        mutableStateListOf<Float>().apply { repeat(items.size) { add(0f) } }
    }
    val tabOffsets = remember(items.size) {
        mutableStateListOf<Float>().apply { repeat(items.size) { add(0f) } }
    }

    val indicatorOffset = remember { Animatable(0f) }
    val indicatorWidth = remember { Animatable(0f) }

    // Indicator animatsiyasi
    LaunchedEffect(
        safeIndex,
        tabWidths.getOrNull(safeIndex),
        tabOffsets.getOrNull(safeIndex)
    ) {
        val w = tabWidths.getOrNull(safeIndex) ?: 0f
        val o = tabOffsets.getOrNull(safeIndex) ?: 0f
        if (w > 0f) {
            launch {
                indicatorOffset.animateTo(o, tween(animationDurationMs, easing = FastOutSlowInEasing))
            }
            launch {
                indicatorWidth.animateTo(w, tween(animationDurationMs, easing = FastOutSlowInEasing))
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val viewportPx = constraints.maxWidth.toFloat()

        // Hech bo'lmaganda viewport / itemCount ga teng bo'lsin (ozdan iborat tablar uchun)
        // Lekin matn katta bo'lsa, table tabiiy enni oladi va scroll ishlaydi
        val minTabWidth = with(density) { (viewportPx / items.size).toDp() }

        // Tanlangan tabni ko'rinishga avto-scroll
        LaunchedEffect(safeIndex, tabWidths.getOrNull(safeIndex)) {
            if (!autoScrollToSelected) return@LaunchedEffect
            val w = tabWidths.getOrNull(safeIndex) ?: 0f
            if (w <= 0f) return@LaunchedEffect

            val tabStart = tabOffsets[safeIndex]
            val tabEnd = tabStart + w
            val viewStart = scrollState.value.toFloat()
            val viewEnd = viewStart + viewportPx

            when {
                tabStart < viewStart -> scrollState.animateScrollTo(tabStart.toInt())
                tabEnd > viewEnd -> scrollState.animateScrollTo((tabEnd - viewportPx).toInt())
            }
        }

        // 1) Pastdagi to'liq border (tanlanmagan rang)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(borderWidth)
                .background(colors.unselectedBorderColor)
                .align(Alignment.BottomStart)
        )

        // 2) Tablar — gorizontal scrollli
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .height(height)
        ) {
            items.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .widthIn(min = minTabWidth)            // ozdan iborat tablar fillmax
                        .height(height)
                        .clickable { onSelect(index) }
                        .padding(horizontal = tabPadding)
                        .onGloballyPositioned { coords ->
                            if (index < tabWidths.size) {
                                tabWidths[index] = coords.size.width.toFloat()
                                tabOffsets[index] = coords.positionInParent().x
                            }
                        },
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

        // 3) Tanlangan indicator — animatsiyali, scroll bilan birga harakatlanadi
        if (indicatorWidth.value > 0f) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset {
                        IntOffset(
                            x = (indicatorOffset.value - scrollState.value).toInt(),
                            y = 0
                        )
                    }
                    .width(with(density) { indicatorWidth.value.toDp() })
                    .height(borderWidth)
                    .background(colors.selectedBorderColor)
            )
        }

        // 4) Chap tomondagi fade (scroll qilingan bo'lsa)
        if (scrollState.value > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(fadeWidth)
                    .height(height - borderWidth)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(fadeColor, Color.Transparent)
                        )
                    )
            )
        }

        // 5) O'ng tomondagi fade (yana tablar borligini bildiradi)
        if (scrollState.value < scrollState.maxValue) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(fadeWidth)
                    .height(height - borderWidth)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, fadeColor)
                        )
                    )
            )
        }
    }
}

@Preview
@Composable
private fun SegmentedTabBarPreview_Many() {
    var selected by remember { mutableStateOf(2) }

    TikonchaParentTheme {
        SegmentedTabBar(
            items = listOf(
                "Hammasi", "Faol", "Tugallangan",
                "Bekor qilingan", "Kechiktirilgan", "Arxiv"
            ),
            selectedIndex = selected,
            onSelect = { selected = it }
        )
    }
}