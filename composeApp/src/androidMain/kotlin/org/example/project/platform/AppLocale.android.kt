package org.example.project.platform

import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual object LocalAppLocale {
    actual val current: String
        get() = Locale.getDefault().toString()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = LocalConfiguration.current
        val ctx = LocalContext.current
        val resources = ctx.resources

        val default = Locale.getDefault()

        val newLocale = when (value) {
            null -> default
            else -> Locale(value) // "uz" yoki "ru"
        }

        Locale.setDefault(newLocale)
        val newConfig = Configuration(configuration)
            newConfig.setLocale(newLocale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(newConfig, resources.displayMetrics)

        return LocalConfiguration.provides(newConfig)
    }
}