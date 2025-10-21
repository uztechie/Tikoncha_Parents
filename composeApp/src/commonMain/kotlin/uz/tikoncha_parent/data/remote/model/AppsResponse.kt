package uz.tikoncha_parent.data.remote.model

data class AppsResponse(
    val id: String,
    val name: String,
    val iconUrl: String?,
    val checked: Boolean
)

data class AppsCategoryDto(
    val id: String,
    val title: String,
    val apps: List<AppsResponse>
)
