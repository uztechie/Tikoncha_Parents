package uz.tikoncha_parent.platform

import kotlinx.datetime.LocalDate
import uz.tikoncha_parent.presentation.domain.model.LanguageType

expect fun LocalDate.getMonthName(locale: LanguageType): String

fun LocalDate.formatMonthYear(language: LanguageType): String {
    return "${getMonthName(language)} $year"
}

fun String.capitalizationFirst(): String{
    if (isBlank()) return this
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}
