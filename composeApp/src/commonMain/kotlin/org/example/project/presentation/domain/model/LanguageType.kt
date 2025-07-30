package org.example.project.presentation.domain.model

import org.jetbrains.compose.resources.DrawableResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.lang_ru
import tikoncha_parents.composeapp.generated.resources.lang_uz

enum class LanguageType(
    val languageCode: String,
    val iconId: DrawableResource,
    val languageName: String
) {
    UZ("uz", iconId = Res.drawable.lang_uz, languageName = "O’zbek tili"),
    RU("ru", iconId = Res.drawable.lang_ru, languageName = "Русский");

    companion object{
        fun getLangType(code: String?): LanguageType{
            return entries.find { it.languageCode == code }?: LanguageType.UZ
        }
    }
}