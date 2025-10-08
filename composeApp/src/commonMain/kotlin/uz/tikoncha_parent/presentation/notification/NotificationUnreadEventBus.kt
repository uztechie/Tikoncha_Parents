
package uz.tikoncha_parent.presentation.notification

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
object NotificationUnreadEventBus {

    sealed interface Event {
        data class SyncCount(val count: Int) : Event

        data class MarkOneRead(val id: Long) : Event

        data object MarkAllRead : Event

        data class NewArrived(val delta: Int = 1) : Event
    }

    private val _events = MutableSharedFlow<Event>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<Event> = _events

    fun tryEmit(event: Event) {
        _events.tryEmit(event)
    }
}
