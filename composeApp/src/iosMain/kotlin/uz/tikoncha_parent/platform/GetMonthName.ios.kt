package uz.tikoncha_parent.platform

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import uz.tikoncha_parent.presentation.domain.model.LanguageType

actual fun LocalDate.getMonthName(locale: LanguageType): String {
    val formatter = NSDateFormatter()
    formatter.locale = NSLocale(locale.languageCode)
    formatter.dateFormat = "MMMM"

    val components = NSDateComponents().apply {
        year = this@getMonthName.year.toLong()
        month = this@getMonthName.month.number.toLong()
        day - this@getMonthName.day.toLong()
    }
    val calendar = NSCalendar.currentCalendar
    val data = calendar.dateFromComponents(components) ?: return ""

    return formatter.stringFromDate(data)
}