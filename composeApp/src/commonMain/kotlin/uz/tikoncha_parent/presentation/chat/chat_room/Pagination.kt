package uz.tikoncha_parent.presentation.chat.chat_room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.LazyListState

@Composable
fun rememberShouldLoadMore(
    listState: LazyListState,
    threshold: Int = 6
): State<Boolean> {
    return remember(listState) {
        derivedStateOf {
            val layout = listState.layoutInfo
            val total = layout.totalItemsCount
            if (total == 0) return@derivedStateOf false

            val lastVisibleIndex = layout.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= total - threshold
        }
    }
}