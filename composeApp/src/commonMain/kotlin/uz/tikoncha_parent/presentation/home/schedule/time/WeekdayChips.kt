package uz.tikoncha_parent.presentation.home.schedule.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.base.CustomChipFilter
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.UltraSmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun WeekdayChips(
    selected: Set<WeekDay>,
    onToggle: (WeekDay) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WeekDay.entries.forEach { day ->
            val on = day in selected
            val label2 = day.label.take(2).uppercase()

            CustomChipFilter(
                selected = on,
                text = label2,
                onClick = { onToggle(day) },
                height = 36.dp,
                minWidth = 42.dp,
                horizontalPadding = 6.dp,
            )
        }
    }
}