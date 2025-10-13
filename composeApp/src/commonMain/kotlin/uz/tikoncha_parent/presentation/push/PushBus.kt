package uz.tikoncha_parent.presentation.push
import uz.tikoncha_parent.domain.model.PushMessage

object PushBus {
    private val listeners = mutableSetOf<PushListener>()
    fun add(listener: PushListener) { listeners += listener }
    fun remove(listener: PushListener) { listeners -= listener }

    fun dispatchMessage(msg: PushMessage) {
        listeners.forEach { it.onMessageReceived(msg) }
    }
}