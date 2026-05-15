package uz.tikoncha_parent.presentation.task.create_task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.presentation.task.model.ImportanceType

@Composable
fun ImportanceSelector(
    selected: ImportanceType,
    onSelect: (ImportanceType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ImportanceButton(
            importance = ImportanceType.MEDIUM,
            selected = selected == ImportanceType.MEDIUM,
            onClick = { onSelect(ImportanceType.MEDIUM) },
            modifier = Modifier.weight(1f)
        )
        ImportanceButton(
            importance = ImportanceType.IMPORTANT,
            selected = selected == ImportanceType.IMPORTANT,
            onClick = { onSelect(ImportanceType.IMPORTANT) },
            modifier = Modifier.weight(1f)
        )
        ImportanceButton(
            importance = ImportanceType.MOST_IMPORTANT,
            selected = selected == ImportanceType.MOST_IMPORTANT,
            onClick = { onSelect(ImportanceType.MOST_IMPORTANT) },
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun ImportanceButton(
    importance: ImportanceType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = when (importance) {
        ImportanceType.MEDIUM -> stringResource(Res.string.o_rtacha)
        ImportanceType.IMPORTANT -> stringResource(Res.string.muhim)
        ImportanceType.MOST_IMPORTANT -> stringResource(Res.string.juda_muhim)
        ImportanceType.NONE -> ""
    }

    val color = when (importance) {
        ImportanceType.MEDIUM -> AppColors.text.accentSuccess         // yashil
        ImportanceType.IMPORTANT -> AppColors.text.accentWarning      // apelsinrang
        ImportanceType.MOST_IMPORTANT -> AppColors.text.accentDanger  // qizil
        ImportanceType.NONE -> AppColors.text.secondary
    }

    val level = when (importance) {
        ImportanceType.MEDIUM -> 1
        ImportanceType.IMPORTANT -> 2
        ImportanceType.MOST_IMPORTANT -> 3
        ImportanceType.NONE -> 0
    }

    // Tanlangan / tanlanmagan ranglar
    val backgroundColor = if (selected) color else AppColors.modal.primary
    val textColor = if (selected) Color.White else color
    val activeBarColor = if (selected) Color.White else color
    val inactiveBarColor = if (selected) {
        Color.White.copy(alpha = 0.4f)   // oq, lekin xira
    } else {
        color.copy(alpha = 0.25f)        // o'sha rangda, xira
    }

    Column(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .singleClick { onClick() }
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = AppTypography.titleSmSemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .width(14.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(
                            if (index < level) activeBarColor
                            else inactiveBarColor
                        )
                )
            }
        }
    }
}


@Preview
@Composable
private fun ImportanceSelectorPreview() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        var selected by remember { mutableStateOf(ImportanceType.NONE) }

        Box(modifier = Modifier.padding(16.dp)) {
            ImportanceSelector(
                selected = selected,
                onSelect = { selected = it }
            )
        }
    }
}