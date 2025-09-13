package uz.tikoncha_parent.presentation.monitoring

import uz.tikoncha_parent.presentation.model.ChatUi

sealed interface MonitorEvent{
    data class SelectChild(val chatUi: ChatUi): MonitorEvent
    data class OnMessageChange(val message: String): MonitorEvent
    object OnSendMessageClick: MonitorEvent

}
