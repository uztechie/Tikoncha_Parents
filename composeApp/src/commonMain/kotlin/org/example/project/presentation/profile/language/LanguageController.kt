package org.example.project.presentation.profile.language

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.project.platform.customAppLocale

import org.example.project.presentation.domain.model.LanguageType

@Stable
class LanguageController {

    private val _current = MutableStateFlow(LanguagePrefs.loadOrDefault())
    val current: StateFlow<LanguageType> = _current

    init {
        customAppLocale = current.value.languageCode
    }
    fun select(newLang: LanguageType) {
        if (newLang == _current.value) return
        _current.value = newLang
        LanguagePrefs.save(newLang)
        customAppLocale = newLang.languageCode
    }
}

/** Ekranlarda controllerga Compose orqali kirish uchun Local */
val LocalLanguageController = staticCompositionLocalOf<LanguageController> {
    error("LanguageController is not provided")
}