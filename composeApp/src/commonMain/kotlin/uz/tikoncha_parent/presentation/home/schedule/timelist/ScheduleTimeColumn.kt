package uz.tikoncha_parent.presentation.home.schedule.timelist

import EDGE_PADDING
import ITEM_HEIGHT
import WHEEL_HEIGHT
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.theme.extendedColor
import kotlin.math.abs

@Composable
fun ScheduleTimeColumn(
    modifier: Modifier = Modifier,
    range: IntRange,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    val items = range.toList()
    val listState = rememberLazyListState()

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
            .width(140.dp)
            .height(WHEEL_HEIGHT)
            .border(
                1.dp,
                MaterialTheme.extendedColor.borderColor,
                RoundedCornerShape(TextFieldCornerRadius)
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
                Box(Modifier.height(ITEM_HEIGHT), contentAlignment = Alignment.Center) {
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
//                .background(
//                    color = LightGrayColor.copy(alpha = 0.3f),
//                    shape = RoundedCornerShape(6.dp)
//                )
                .zIndex(1f)
        )
    }
}