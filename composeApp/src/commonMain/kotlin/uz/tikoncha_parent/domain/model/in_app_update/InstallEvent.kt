package uz.tikoncha_parent.domain.model.in_app_update

sealed class InstallEvent {
    data object Downloaded : InstallEvent()
    data class Downloading(val bytesDownloaded: Long, val totalBytes: Long) : InstallEvent()
    data class Failed(val code: Int) : InstallEvent()
}