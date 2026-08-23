package uz.tikoncha_parent.domain.model

data class ChildLinkCode(
    val code: String,
    val expiresAtMillis: Long,
)