package uz.tikoncha_parent.data.player


import kotlinx.coroutines.flow.StateFlow
import uz.tikoncha_parent.domain.model.player.PlayerState

expect class PlayerEngine {
    val state: StateFlow<PlayerState>
    fun setSource(url: String, autoPlay: Boolean, startPositionMs: Long)
    fun play()
    fun pause()
    fun seekTo(ms: Long)
    fun setMuted(muted: Boolean)
    fun release()
}