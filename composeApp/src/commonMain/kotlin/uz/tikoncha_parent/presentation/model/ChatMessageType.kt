package uz.tikoncha_parent.presentation.model

enum class ChatMessageType {
    TEXT,
    AUDIO,
    VIDEO,
    IMAGE,
    FILE;
    companion object{
        fun getChatMessageTypeByName(name: String): ChatMessageType =
            entries.find { it.name == name } ?: TEXT
    }
}