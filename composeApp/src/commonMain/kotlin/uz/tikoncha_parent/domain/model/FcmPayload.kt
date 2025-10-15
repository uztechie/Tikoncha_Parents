package uz.tikoncha_parent.domain.model


import kotlinx.serialization.Serializable

@Serializable
data class FcmPayload(
    val title: String = "",
    val message: String = "",
    val type: PayloadType = PayloadType.GENERAL,
    val body: FcmPayloadBody? = null
)

@Serializable
enum class PayloadType {
    TODO,
    NEWS,
    CHAT,
    GENERAL,
}

@Serializable
data class FcmPayloadBody(
    val app: AppBlock? = null,
    val todo: TodoBlock? = null,
    val news: NewsBlock? = null,
    val message: MessageBlock? = null
)

@Serializable
data class AppBlock(
    val package_name: String,
    val action: AppAction
)

@Serializable
enum class AppAction {
    ALLOW,
    DENY
}

@Serializable
data class TodoBlock(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val importance: String? = null,
    val due_date: String? = null
)

@Serializable
data class NewsBlock(
    val id: String? = null,
    val title: String? = null
)
@Serializable
data class MessageBlock(
    val message_id: String? = null,
    val sender_id: String? = null,
    val chat_id: String? = null,
    val chat_title: String? = null,
    val text: String? = null
)
