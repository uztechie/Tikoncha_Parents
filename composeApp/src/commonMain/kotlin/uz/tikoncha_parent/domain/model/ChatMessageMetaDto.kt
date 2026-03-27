package uz.tikoncha_parent.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageMetaDto(
    val duration: Long? = null,
    val amplitude: String? = null,
)
