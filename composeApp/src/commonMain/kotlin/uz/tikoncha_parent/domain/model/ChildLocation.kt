package uz.tikoncha_parent.domain.model

data class ChildLocation(
    val childId: String,
    val firstName: String?,
    val lastName: String?,
    val avatarUrl: String?,
    val latitude: Double?,
    val longitude: Double?,
    val updatedAt: String?,
)