
package uz.tikoncha_parent.platform

import uz.tikoncha_parent.presentation.domain.model.LanguageType

expect fun getWeekDays(language: LanguageType): List<String>
