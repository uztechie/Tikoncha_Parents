package org.example.project.presentation.domain.model

import org.jetbrains.compose.resources.DrawableResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.lang_ru
import tikoncha_parents.composeapp.generated.resources.lang_uz

enum class LanguageType(
    val iconId: DrawableResource,
    val languageName: String

) {
    UZ(iconId = Res.drawable.lang_uz, languageName = "O’zbek tili"),
    RU(iconId = Res.drawable.lang_ru, languageName = "Русский"),
}