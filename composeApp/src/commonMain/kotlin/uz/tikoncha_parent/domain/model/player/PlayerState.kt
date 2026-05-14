package uz.tikoncha_parent.domain.model.player


data class PlayerState(
    val isPlaying: Boolean = false,
    val isMuted: Boolean = false,
    val isBuffering: Boolean = false,
    val isReady: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferedMs: Long = 0L,
    val error: String? = null
)