package uz.tikoncha_parent.presentation.task.model

import kotlinx.serialization.Serializable

@Serializable
enum class ImportanceType {
    MEDIUM,
    IMPORTANT,
    MOST_IMPORTANT,
    NONE
}