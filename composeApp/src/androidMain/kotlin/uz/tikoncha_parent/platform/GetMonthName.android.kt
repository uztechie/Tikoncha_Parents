@file:Suppress("DEPRECATION")

package uz.tikoncha_parent.platform

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import java.time.format.TextStyle
import java.util.Locale

actual fun LocalDate.getMonthName(locale: LanguageType): String {
    val javaDate = java.time.LocalDate.of(year, month.number, day)
    return javaDate.month
        .getDisplayName(TextStyle.FULL, Locale(locale.languageCode))
        .capitalizationFirst()
}