package uz.tikoncha_parent.domain.model.apps

data class InstalledApp(
    val packageName: String,
    val appName: String,
    val category: String? = null,
    val hasIcon: Boolean = false,
)
