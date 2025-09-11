package uz.tikoncha_parent.presentation.model

import kotlinx.serialization.Serializable

@Serializable
enum class ChatType {
    BOT,
    PARENT_CHILD,
    CLASS,
    SIBLINGS,
    NONE
}