package org.example.project.presentation.profile.language

import dev.icerock.moko.resources.desc.StringDesc

object LanguageManager {
    fun setLanguage(langCode: String) {
        StringDesc.localeType = StringDesc.LocaleType.Custom(langCode)
    }
}