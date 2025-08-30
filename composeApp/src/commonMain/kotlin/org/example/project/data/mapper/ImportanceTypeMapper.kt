package org.example.project.data.mapper

import org.example.project.presentation.task.ImportanceType

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