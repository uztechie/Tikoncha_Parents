package uz.tikoncha_parent.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.domain.model.player.PlayerState

interface PlayerRepository {
    fun observeState(): Flow<PlayerState>
    fun setSource(url: String, autoPlay: Boolean = true, startPositionMs: Long = 0L)
    fun play()
    fun pause()
    fun togglePlayPause()
    fun seekTo(ms: Long)
    fun seekBy(deltaMs: Long)
    fun setMuted(muted: Boolean)
    fun toggleMute()
    fun release()
}