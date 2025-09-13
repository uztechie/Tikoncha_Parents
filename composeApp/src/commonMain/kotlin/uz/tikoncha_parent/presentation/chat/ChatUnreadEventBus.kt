package uz.tikoncha_parent.presentation.chat

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object ChatUnreadEventBus {
    sealed interface ChatUnreadEvent{
        data class MessageReceived(val messageId: String):
            ChatUnreadEvent
        data class MessageRead(val messageId: String):
            ChatUnreadEvent
    }

    private val _events = MutableSharedFlow<ChatUnreadEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events: SharedFlow<ChatUnreadEvent> = _events
    fun tryEmit(event: ChatUnreadEvent){
        _events.tryEmit(event)

    }
}