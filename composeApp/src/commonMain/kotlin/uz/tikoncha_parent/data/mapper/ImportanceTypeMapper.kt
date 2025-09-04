package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.presentation.task.ImportanceType

fun ImportanceType.toServerType(): String {
    return when(this){
        ImportanceType.MEDIUM -> {"LOW"}
        ImportanceType.IMPORTANT -> {"MEDIUM"}
        ImportanceType.MOST_IMPORTANT -> {"HIGH"}
        ImportanceType.NONE -> {""}
    }
}

fun String.toImportanceType(): ImportanceType{
    return when(this){
        "LOW" -> ImportanceType.MEDIUM
        "MEDIUM" -> ImportanceType.IMPORTANT
        "HIGH" -> ImportanceType.MOST_IMPORTANT
        else -> ImportanceType.NONE
    }
}