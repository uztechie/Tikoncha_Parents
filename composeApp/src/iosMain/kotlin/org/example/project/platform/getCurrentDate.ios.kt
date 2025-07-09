package org.example.project.platform

import androidx.compose.ui.text.intl.Locale
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeInterval
import platform.Foundation.dateWithTimeIntervalSince1970

actual fun reformatDateTime(millis: Long, pattern: String): String {
    val date = NSDate.dateWithTimeIntervalSince1970(millis/1000.0)
    val formatter = NSDateFormatter()
    formatter.dateFormat = pattern
    formatter.locale = NSLocale.currentLocale
    return formatter.stringFromDate(date)
}