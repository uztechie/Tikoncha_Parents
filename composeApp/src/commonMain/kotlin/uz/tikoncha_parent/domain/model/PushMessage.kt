package uz.tikoncha_parent.domain.model

data class PushMessage(
    val title: String?,
    val body: String?,
    val data: Map<String, String> = emptyMap()
)

