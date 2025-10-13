package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.domain.model.PushMessage


interface PushListener {
    fun onMessageReceived(message: PushMessage)
}