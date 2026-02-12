package uz.tikoncha_parent.platform

import platform.Foundation.*
import uz.tikoncha_parent.common.Util.shiftToMonday
import uz.tikoncha_parent.presentation.domain.model.LanguageType

actual fun getWeekDays(language: LanguageType): List<String> {
    val formatter = NSDateFormatter().apply {
        locale = NSLocale(language.languageCode)
    }
    val symbols = formatter.shortWeekdaySymbols.toList()

    return symbols
        .map {it.toString()}
        .map { it.replaceFirstChar { char -> char.uppercase() } }
        .shiftToMonday()
}