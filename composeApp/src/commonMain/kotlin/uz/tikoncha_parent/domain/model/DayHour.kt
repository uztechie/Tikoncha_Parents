package uz.tikoncha_parent.domain.model

import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.kunlik
import tikoncha_parents.composeapp.generated.resources.minut
import tikoncha_parents.composeapp.generated.resources.soatlik

enum class DayHour(
    val resId: StringResource
) {
    DAY(resId = Res.string.kunlik),
    HOUR(resId = Res.string.soatlik);

    companion object {
        fun getDayHourByKey(key: String?): DayHour {
            return entries.find { it.name == key } ?: DAY
        }
    }
}