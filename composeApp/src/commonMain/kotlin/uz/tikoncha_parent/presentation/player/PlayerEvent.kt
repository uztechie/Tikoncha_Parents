package uz.tikoncha_parent.presentation.player

sealed interface PlayerEvent {
    data class Load(
        val url: String,
        val autoPlay: Boolean = true,
        val startPositionMs: Long = 0L
    ) : PlayerEvent

    data object TogglePlayPause : PlayerEvent
    data class SeekTo(val ms: Long) : PlayerEvent
    data class SeekBy(val deltaMs: Long) : PlayerEvent
    data object ToggleMute : PlayerEvent
    data object Close : PlayerEvent
    data object Release : PlayerEvent
    data object ClearError : PlayerEvent
}