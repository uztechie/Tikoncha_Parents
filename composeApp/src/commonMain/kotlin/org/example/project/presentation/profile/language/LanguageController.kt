package org.example.project.presentation.profile.language

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import org.example.project.platform.customAppLocale
import org.example.project.presentation.domain.model.LanguageType

@Stable
class LanguageController(initial: LanguageType = LanguagePrefs.loadOrDefault()) {
    var current by mutableStateOf(initial)
        private set

    fun select(newLang: LanguageType) {
        if (newLang == current) return
        current = newLang
        LanguagePrefs.save(newLang)
        // Runtime locale ni yangilash triggeri:
        customAppLocale = newLang.languageCode
    }
}

/** Ekranlarda controllerga Compose orqali kirish uchun Local */
val LocalLanguageController = staticCompositionLocalOf<LanguageController> {
    error("LanguageController is not provided")
}