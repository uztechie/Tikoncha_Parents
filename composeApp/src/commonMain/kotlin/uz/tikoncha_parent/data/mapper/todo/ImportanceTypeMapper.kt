package uz.tikoncha_parent.data.mapper.todo

import uz.tikoncha_parent.domain.model.todo.Importance
import uz.tikoncha_parent.presentation.task.model.ImportanceType

/** ImportanceType (UI) → Importance (domain) */
fun ImportanceType.toImportance(): Importance = when (this) {
    ImportanceType.MEDIUM -> Importance.LOW            // O'rtacha
    ImportanceType.IMPORTANT -> Importance.MEDIUM      // Muhim
    ImportanceType.MOST_IMPORTANT -> Importance.HIGH   // Juda muhim
    ImportanceType.NONE -> Importance.MEDIUM           // default
}

/** Importance (domain) → ImportanceType (UI) */
fun Importance.toImportanceType(): ImportanceType = when (this) {
    Importance.LOW -> ImportanceType.MEDIUM
    Importance.MEDIUM -> ImportanceType.IMPORTANT
    Importance.HIGH -> ImportanceType.MOST_IMPORTANT
    Importance.CRITICAL -> ImportanceType.MOST_IMPORTANT
}

/** ImportanceType → server String (eski toServerType() o'rniga ham ishlatish mumkin) */
fun ImportanceType.toServerType(): String = toImportance().apiValue