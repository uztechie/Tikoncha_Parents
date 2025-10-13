package uz.tikoncha_parent.presentation.push
import uz.tikoncha_parent.domain.model.PushMessage

class KmpPushBridge {
    fun onMessage(title: String?, body: String?, dateMillis: Long) {
        PushBus.dispatchMessage(PushMessage(title, body, emptyMap()))
    }
    companion object { val shared = KmpPushBridge() }

}