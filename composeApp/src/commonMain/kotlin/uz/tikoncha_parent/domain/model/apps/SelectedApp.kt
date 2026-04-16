package uz.tikoncha_parent.domain.model.apps

data class SelectedApp(
    val packageName: String,
    val appName: String,
    val category: String?
)