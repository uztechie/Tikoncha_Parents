package uz.tikoncha_parent.presentation.push

import uz.tikoncha_parent.data.local.AppSettings

class KmpTokenBridge {
    fun onNewToken(token: String){
        AppSettings.fcmToken = token
        FcmTokenRegister.submit(token)
    }

    companion object{
        val shared = KmpTokenBridge()
    }
}