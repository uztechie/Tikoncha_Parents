package uz.tikoncha_parent.presentation.in_app_update

import uz.tikoncha_parent.domain.model.in_app_update.UpdateType

data class UpdateUiState(
    val isChecking: Boolean = false,

    val showUpdateDialog: Boolean = false,
    val showCard: Boolean = false,
    val allowedTypes: Set<UpdateType> = emptySet(),
    val recommendedType: UpdateType? = null,

    val isDownloading: Boolean = false,
    val bytesDownloaded: Long = 0L,
    val totalBytes: Long = 0L,

    val flexibleDownloaded: Boolean = false,

    val message: String? = null
) {
    val downloadProgress: Float
        get() = if (totalBytes > 0) bytesDownloaded.toFloat() / totalBytes else 0f
}
