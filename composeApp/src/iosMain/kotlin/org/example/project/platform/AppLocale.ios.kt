package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual object LocalAppLocale {

    private const val LANG_KEY = "AppleLanguages"
    private val defaultLang: String = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "uz"
    private val Local = staticCompositionLocalOf { defaultLang }

    actual val current: String
        @Composable get() = Local.current


    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLang = value ?: defaultLang
        val prefs = NSUserDefaults.standardUserDefaults
        if (value == null) {
            prefs.removeObjectForKey(LANG_KEY)
        } else {
            // AppleLanguages — til kodlar ro‘yxati, masalan ["ru"] yoki ["uz"]
            prefs.setObject(listOf(newLang), forKey = LANG_KEY)
        }
        return Local.provides(newLang)
    }
}