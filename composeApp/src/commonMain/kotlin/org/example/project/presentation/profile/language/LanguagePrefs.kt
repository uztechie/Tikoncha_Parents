package org.example.project.presentation.profile.language

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.example.project.presentation.domain.model.LanguageType

object LanguagePrefs {
    private const val KEY = "app_language"
    private val settings: Settings = Settings() // no-arg: Android=SharedPrefs, iOS=UserDefaults

    // Faqat kodni saqlash/olish:
    fun saveCode(code: String) { settings[KEY] = code }
    fun loadCodeOrNull(): String? = settings.getStringOrNull(KEY)
    fun clear() { settings.remove(KEY) }

    // Qulaylik uchun LanguageType bilan ishlaydigan helperlar:
    fun save(type: LanguageType) = saveCode(type.languageCode)
    fun loadOrDefault(default: LanguageType = LanguageType.UZ): LanguageType =
        loadCodeOrNull()
            ?.let { c -> LanguageType.values().firstOrNull { it.languageCode == c } }
            ?: default
}