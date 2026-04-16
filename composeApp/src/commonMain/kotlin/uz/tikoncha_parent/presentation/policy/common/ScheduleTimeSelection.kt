package uz.tikoncha_parent.presentation.policy.common

import EDGE_PADDING
import ITEM_HEIGHT
import WHEEL_HEIGHT
import WHEEL_WIDTH
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.math.abs

@Composable
fun ScheduleTimeSelection(
    modifier: Modifier = Modifier,
    range: IntRange,
    selected: Int,
    onSelected: (Int) -> Unit,
    label: String = "",
    padding: Dp = 0.dp
) {
    val items = range.toList()
    val listState = rememberLazyListState()
    
    var boxWidth by remember { mutableStateOf(0.dp) }


    var isFirstLaunch by remember { mutableStateOf(true) }

    LaunchedEffect(listState) {
        snapshotFlow {
            val info = listState.layoutInfo
            val centerY = info.viewportStartOffset + info.viewportSize.height / 2
            // Markaz CHIZIG'INI ichiga olgan itemni aniq topamiz:
            info.visibleItemsInfo.firstOrNull { item ->
                val mid = item.offset + item.size / 2
                // item markazga eng yaqin va markaz chizig'i itemning vertikal chegaralari ichida
                centerY in item.offset..(item.offset + item.size)
            }?.index
            // Agar yuqoridagi topilmasa (masalan, layout o'zgarishi), "eng yaqin"iga qaytamiz:
                ?: info.visibleItemsInfo.minByOrNull { item ->
                    abs((item.offset + item.size / 2) - centerY)
                }?.index
        }
            .filterNotNull()
            .distinctUntilChanged()                // har BIR indeks markazga kelganda 1 marta
            .map { idx -> range.elementAtOrNull(idx) }
            .filterNotNull()
            .collect { value ->
                onSelected(value)                  // <<— EQUAL CHECK OLIB TASHLANDI
            }
    }

    // --- Yangi: markazdagi tanlangan raqamning kengligini o‘lchaymiz
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val selectedText = remember(selected) { selected.toString().padStart(2, '0') }

    // Markazdagi item uchun siz ishlatayotgan uslub: 16.sp, W500
    val selectedStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.W500
    )

    val selectedTextWidthDp by remember(selectedText, selectedStyle) {
        mutableStateOf(
            with(density) {
                textMeasurer
                    .measure(AnnotatedString(selectedText), style = selectedStyle)
                    .size.width.toDp()
            }
        )
    }


    // Boshlanishda to'g'ridan-to'g'ri scroll qilish
    LaunchedEffect(Unit) {
        if (isFirstLaunch) {
            listState.scrollToItem(index = selected)
            onSelected(selected)
            isFirstLaunch = false
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned{
                boxWidth = with(density) { it.size.width.toDp() }
            }
            .width(WHEEL_WIDTH)
            .height(WHEEL_HEIGHT)
            .border(
                1.dp,
                MaterialTheme.extendedColor.borderColor,
                RoundedCornerShape(CardCornerRadius)
            )

    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = EDGE_PADDING),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
        ) {
            items(items.size) { index ->
                val value = items[index]
                val isSelected = value == selected
                Row(
                    modifier = Modifier
                        .height(ITEM_HEIGHT),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = value.toString().padStart(2, '0'),
                        fontSize = if (isSelected) 16.sp else 12.sp,
                        fontWeight = if (isSelected) FontWeight.W500 else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.extendedColor.onBackgroundColor else MaterialTheme.extendedColor.hintColor,
                        modifier = Modifier.padding(vertical = 0.dp)
                    )
                }
            }
        }

        // Markazni ko‘rsatish uchun fon
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(ITEM_HEIGHT)
                .zIndex(1f)
        )

        Box(
            modifier = Modifier
                .offset(x = boxWidth/2 + selectedTextWidthDp + padding)
                .zIndex(2f)
                .align(Alignment.CenterStart)
        ) {
            CustomText(
                text = label,
                color = MaterialTheme.extendedColor.hintColor
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ScheduleTimeSelection(
        range = 0..23,
        selected = 12,
        onSelected = {},
        label = "soat"
    )
}