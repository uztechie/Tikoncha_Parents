package uz.tikoncha_parent.presentation.notification

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object NotificationRefreshEventBus {
    private val _refresh = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val refresh: SharedFlow<Unit> = _refresh

    fun notifyRefresh() { _refresh.tryEmit(Unit) }
}