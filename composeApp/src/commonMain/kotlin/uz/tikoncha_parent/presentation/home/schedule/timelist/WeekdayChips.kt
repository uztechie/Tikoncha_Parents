package uz.tikoncha_parent.presentation.home.schedule.timelist

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
import tikoncha_parents.composeapp.generated.resources.ch
import tikoncha_parents.composeapp.generated.resources.du
import tikoncha_parents.composeapp.generated.resources.har_kuni
import tikoncha_parents.composeapp.generated.resources.ju
import tikoncha_parents.composeapp.generated.resources.kunlar
import tikoncha_parents.composeapp.generated.resources.pa
import tikoncha_parents.composeapp.generated.resources.se
import tikoncha_parents.composeapp.generated.resources.sh
import tikoncha_parents.composeapp.generated.resources.ya
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.WeekDay
import uz.tikoncha_parent.presentation.base.CustomChipFilter
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun WeekdayChips(
    selected: Set<WeekDay>,
    onToggle: (WeekDay) -> Unit,
) {

    val labels = if (selected.size == 7){
        stringResource(Res.string.har_kuni)
    }
    else{
        selected.map { weekdayLabel(it) }.joinToString(", ")
    }

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
                text = labels,
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
            WeekDay.entries.forEach { day ->
                val on = day in selected
                val label = weekdayLabel(day)

                CustomChipFilter(
                    selected = on,
                    text = label,
                    onClick = { onToggle(day) },
                    height = 36.dp,
                    minWidth = 36.dp,
                )
            }
        }
    }


}


@Composable
private fun weekdayLabel(day: WeekDay): String {
    return when(day) {
        WeekDay.MON -> stringResource(Res.string.du)
        WeekDay.TUE -> stringResource(Res.string.se)
        WeekDay.WED -> stringResource(Res.string.ch)
        WeekDay.THU -> stringResource(Res.string.pa)
        WeekDay.FRI -> stringResource(Res.string.ju)
        WeekDay.SAT -> stringResource(Res.string.sh)
        WeekDay.SUN -> stringResource(Res.string.ya)
    }
}