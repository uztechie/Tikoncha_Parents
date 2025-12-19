package uz.tikoncha_parent.domain.model


import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class FcmPayload(
    val title: String = "",
    val message: String = "",
    val type: PayloadType = PayloadType.GENERAL,
    val body: FcmPayloadBody? = null
)

@Serializable(with = PayloadTypeSerializer::class)
enum class PayloadType(val id: String) {
    TODO(id = "TODO"),
    NEWS(id = "NEWS"),
    CHAT(id = "CHAT"),
    GENERAL(id = "GENERAL"),
    CHILD_REQUEST(id = "CHILD-REQUEST");

    companion object {
        fun fromId(value: String?): PayloadType =
            entries.firstOrNull { it.id.equals(value, ignoreCase = true) }
                ?: entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: GENERAL
    }

}

object PayloadTypeSerializer : KSerializer<PayloadType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("PayloadType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PayloadType {
        val v = decoder.decodeString()
        return PayloadType.fromId(v)
    }

    override fun serialize(encoder: Encoder, value: PayloadType) {
        // Serverga id yuborish kerak bo‘lsa:
        encoder.encodeString(value.id)
        // Agar name yuborish kerak bo‘lsa: encoder.encodeString(value.name)
    }
}

@Serializable
data class FcmPayloadBody(
    val app: AppBlock? = null,
    val todo: TodoBlock? = null,
    val news: NewsBlock? = null,
    val message: MessageBlock? = null,
    val childRequest: ChildRequestBlock? = null
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

@Serializable
data class ChildRequestBlock(
    val id: String? = null,
    val child_user_id: String? = null,
    val action: String? = null,
    val packages: List<String> = emptyList()
)