package uz.saidburxon.newedu.presentation.feature.chat

sealed interface ChatEvent {

    data class OnMessageChange(val message: String): ChatEvent
    data object SendMessage: ChatEvent

}