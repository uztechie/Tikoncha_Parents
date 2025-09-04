package uz.tikoncha_parent.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual object LocalAppLocale {
    private const val LANG_KEY = "AppleLanguages"
    private val default = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: ""
    private val Local = staticCompositionLocalOf { default }


    actual val current: String
    @Composable get() = Local.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newTag = value ?: default
        if (value == null) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey(LANG_KEY)
        } else {
            NSUserDefaults.standardUserDefaults.setObject(listOf(newTag), forKey = LANG_KEY)
        }
        return Local.provides(newTag)
    }
}