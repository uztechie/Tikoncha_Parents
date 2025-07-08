import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.presentation.base.CustomOutlinedButton
import org.example.project.presentation.base.theme.*
import org.example.project.ui.ChatMessageBackgroundColor
import org.example.project.ui.PrimaryColor
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton

@Composable
fun TimePickerDialog(
    show: Boolean,
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit
) {
    var selectedHour by remember { mutableStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableStateOf(initialTime.minute) }

    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.tugash_vaqtini_belgilang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TimeColumn(
                            range = 0..23,
                            selected = selectedHour,
                            onSelected = { selectedHour = it }
                        )
                        TimeColumn(
                            range = 0..59,
                            selected = selectedMinute,
                            onSelected = { selectedMinute = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {

                        CustomOutlinedButton(
                            text = stringResource(Res.string.bekor_qilish),
                            onClick = onDismiss,
                            modifier = Modifier
                                .height(DialogButtonHeight)
                                .fillMaxWidth(),
                            textColor = org.example.project.presentation.base.theme.PrimaryColor,
                            borderColor = org.example.project.presentation.base.theme.PrimaryColor,
                            shape = RoundedCornerShape(ButtonDialogCornerRadius)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        CustomButton(
                            onClick = {
                                onTimeSelected(LocalTime(selectedHour, selectedMinute))
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp,
                                    org.example.project.presentation.base.theme.PrimaryColor, RoundedCornerShape(ButtonDialogCornerRadius))
                                .height(DialogButtonHeight),
                            text = stringResource(Res.string.saqlash),
                            shape = RoundedCornerShape(ButtonDialogCornerRadius)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun TimeColumn(
    range: IntRange,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    val items = range.toList()
    val listState = rememberLazyListState()

    var isFirstLaunch by remember { mutableStateOf(true) }

    // Scroll tugagach, markazdagi elementni olish
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val centerY = listState.layoutInfo.viewportStartOffset + listState.layoutInfo.viewportSize.height / 2
            val centeredItem = visibleItems.minByOrNull { kotlin.math.abs((it.offset + it.size / 2) - centerY) }
            centeredItem?.let { centered ->
                val value = items.getOrNull(centered.index)
                if (value != null && value != selected) {
                    onSelected(value)
                }
            }
        }
    }

    // Boshlanishda to'g'ridan-to'g'ri scroll qilish
    LaunchedEffect(Unit) {
        if (isFirstLaunch) {
            listState.scrollToItem(index = selected)
            isFirstLaunch = false
        }
    }

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(70.dp)
            .border(1.dp, PrimaryColor, RoundedCornerShape(12.dp))
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
        ) {
            items(items.size) { index ->
                val value = items[index]
                val isSelected = value == selected
                Text(
                    text = value.toString().padStart(2, '0'),
                    fontSize = if (isSelected) 16.sp else 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.Black else Color.LightGray,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        // Markazni ko‘rsatish uchun fon
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(24.dp)
                .background(
                    color = ChatMessageBackgroundColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp)
                )
                .zIndex(1f)
        )
    }
}





