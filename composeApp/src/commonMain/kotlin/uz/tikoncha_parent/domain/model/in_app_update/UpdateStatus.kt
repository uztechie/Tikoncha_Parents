package uz.tikoncha_parent.domain.model.in_app_update

sealed class UpdateStatus {
    data object NotSupported : UpdateStatus() // Play Store emas / xatolik
    data object NoUpdate : UpdateStatus()

    data class UpdateAvailable(
        val allowedTypes: Set<UpdateType>,
        val versionCodeAvailable: Int? = null
    ) : UpdateStatus()

    data object Downloaded : UpdateStatus() // Flexible update downloaded bo‘ldi
    data class Failed(val message: String? = null) : UpdateStatus()
}