package uz.tikoncha_parent.presentation.policy.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.har_kuni
import tikoncha_parents.composeapp.generated.resources.kunlar
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.presentation.base.CustomChipFilter
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.theme.extendedColor



@Composable
fun WeekdayChips(
    chips: List<WeekDayChipUi>,
    onToggle: (WeekDay) -> Unit,
) {


    val selectedLabels = chips.filter { it.selected }.map { it.day.weekdayLabel() }
    val header = if (selectedLabels.size == 7) stringResource(Res.string.har_kuni)
    else selectedLabels.joinToString(", ")

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomText(
                text = stringResource(Res.string.kunlar),
                color = MaterialTheme.extendedColor.hintColor
            )
            Spacer(Modifier.weight(1f))
            CustomText(
                text = header,
                color = MaterialTheme.extendedColor.hintColor
            )
        }

        SpaceUltraSmall()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            chips.forEach { chip ->
                CustomChipFilter(
                    selected = chip.selected,
                    text = chip.day.weekdayLabel(),
                    onClick = { if (chip.enabled) onToggle(chip.day) },
                    height = 36.dp,
                    minWidth = 36.dp,
                    enabled = chip.enabled
                )
            }
        }
    }
}