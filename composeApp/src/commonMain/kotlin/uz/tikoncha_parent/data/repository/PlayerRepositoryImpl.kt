package uz.tikoncha_parent.data.repository

import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.data.player.PlayerEngine
import uz.tikoncha_parent.domain.model.player.PlayerState
import uz.tikoncha_parent.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val engine: PlayerEngine
) : PlayerRepository {

    override fun observeState(): Flow<PlayerState> = engine.state

    override fun setSource(url: String, autoPlay: Boolean, startPositionMs: Long) =
        engine.setSource(url, autoPlay, startPositionMs)

    override fun play() = engine.play()
    override fun pause() = engine.pause()

    override fun togglePlayPause() {
        if (engine.state.value.isPlaying) engine.pause() else engine.play()
    }

    override fun seekTo(ms: Long) = engine.seekTo(ms)

    override fun seekBy(deltaMs: Long) {
        engine.seekTo(engine.state.value.positionMs + deltaMs)
    }

    override fun setMuted(muted: Boolean) = engine.setMuted(muted)
    override fun toggleMute() = setMuted(!engine.state.value.isMuted)
    override fun release() = engine.release()
}