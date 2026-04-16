package uz.tikoncha_parent.domain.model.apps

data class CategoryGroup(
    val id: String,
    val apps: List<InstalledApp>,
)