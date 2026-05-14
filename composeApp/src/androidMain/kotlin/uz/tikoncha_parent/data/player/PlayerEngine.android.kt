package uz.tikoncha_parent.data.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.player.PlayerState

actual class PlayerEngine(context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _state = MutableStateFlow(PlayerState())
    actual val state: StateFlow<PlayerState> = _state.asStateFlow()

    internal val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                _state.update {
                    it.copy(
                        isBuffering = playbackState == Player.STATE_BUFFERING,
                        isReady = playbackState == Player.STATE_READY,
                        durationMs = if (duration > 0) duration else it.durationMs
                    )
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                _state.update { it.copy(error = error.message ?: "Playback error") }
            }

            override fun onVolumeChanged(volume: Float) {
                _state.update { it.copy(isMuted = volume == 0f) }
            }
        })
    }

    init {
        scope.launch {
            while (isActive) {
                _state.update {
                    it.copy(
                        positionMs = exoPlayer.currentPosition.coerceAtLeast(0),
                        bufferedMs = exoPlayer.bufferedPosition.coerceAtLeast(0),
                        durationMs = if (exoPlayer.duration > 0) exoPlayer.duration
                        else it.durationMs
                    )
                }
                delay(250)
            }
        }
    }

    actual fun setSource(url: String, autoPlay: Boolean, startPositionMs: Long) {
        exoPlayer.setMediaItem(MediaItem.fromUri(url), startPositionMs)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = autoPlay
    }

    actual fun play() { exoPlayer.play() }
    actual fun pause() { exoPlayer.pause() }

    actual fun seekTo(ms: Long) {
        val max = exoPlayer.duration.coerceAtLeast(0)
        exoPlayer.seekTo(ms.coerceIn(0, max))
    }

    actual fun setMuted(muted: Boolean) {
        exoPlayer.volume = if (muted) 0f else 1f
    }

    actual fun release() {
        // single sifatida ushlab turamiz, faqat manbani tozalaymiz
        exoPlayer.pause()
        exoPlayer.clearMediaItems()
        _state.update { PlayerState() }
    }
}