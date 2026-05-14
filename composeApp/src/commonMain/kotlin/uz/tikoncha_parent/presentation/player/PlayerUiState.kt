package uz.tikoncha_parent.presentation.player


data class PlayerUiState(
    val videoUrl: String = "",
    val isReady: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPlaying: Boolean = false,
    val isMuted: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferedMs: Long = 0L,
    val shouldClose: Boolean = false
)