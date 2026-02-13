package uz.tikoncha_parent.presentation.chat.chat_list

sealed class ChatEvent {
    data class OnScreenOpened(val screen: String) : ChatEvent()
    data class OnScreenClosed(val screen: String) : ChatEvent()
    data object Refresh : ChatEvent()
}