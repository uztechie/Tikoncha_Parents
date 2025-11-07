package uz.tikoncha_parent.domain.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ch
import tikoncha_parents.composeapp.generated.resources.du
import tikoncha_parents.composeapp.generated.resources.ju
import tikoncha_parents.composeapp.generated.resources.pa
import tikoncha_parents.composeapp.generated.resources.se
import tikoncha_parents.composeapp.generated.resources.sh
import tikoncha_parents.composeapp.generated.resources.ya

enum class WeekDay(val num: Int) {
    MON(1), TUE(2), WED(3), THU(4), FRI(5), SAT(6), SUN(7);

    companion object {
        fun fromNum(n: Int): WeekDay = entries.first { it.num == n }
        val ordered: List<WeekDay> = listOf(MON, TUE, WED, THU, FRI, SAT, SUN)
    }
}

@Composable
fun WeekDay.weekdayLabel(): String {
    return when(this) {
        WeekDay.MON -> stringResource(Res.string.du)
        WeekDay.TUE -> stringResource(Res.string.se)
        WeekDay.WED -> stringResource(Res.string.ch)
        WeekDay.THU -> stringResource(Res.string.pa)
        WeekDay.FRI -> stringResource(Res.string.ju)
        WeekDay.SAT -> stringResource(Res.string.sh)
        WeekDay.SUN -> stringResource(Res.string.ya)
    }
}