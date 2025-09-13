package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val chat_id: String,
    val type: String,
    val text: String,
){

}
