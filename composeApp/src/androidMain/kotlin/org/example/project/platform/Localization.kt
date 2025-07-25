package org.example.project.platform

import android.content.Context
import android.os.LocaleList
import java.util.Locale

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class Localization(
    private val context: Context
) {
    actual fun applyLanguage(ios: String) {
        val locale = Locale(ios)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocales(LocaleList(locale))
    }
}