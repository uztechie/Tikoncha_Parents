package org.example.project.presentation.task

import kotlinx.serialization.Serializable

@Serializable
enum class ImportanceType {
    MEDIUM,
    IMPORTANT,
    MOST_IMPORTANT,
    NONE
}