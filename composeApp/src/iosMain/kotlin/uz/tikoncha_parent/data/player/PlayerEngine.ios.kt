package uz.tikoncha_parent.data.player

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemStatusReadyToPlay
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.error
import platform.AVFoundation.loadedTimeRanges
import platform.AVFoundation.muted
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.rate
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.setMuted
import platform.AVFoundation.status
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSURL
import platform.Foundation.NSValue
import platform.darwin.dispatch_get_main_queue
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import platform.AVFoundation.CMTimeRangeValue
import platform.AVFoundation.removeTimeObserver
import uz.tikoncha_parent.domain.model.player.PlayerState

@OptIn(ExperimentalForeignApi::class)
actual class PlayerEngine {

    private val _state = MutableStateFlow(PlayerState())
    actual val state: StateFlow<PlayerState> = _state.asStateFlow()

    internal val avPlayer: AVPlayer = AVPlayer()
    private var timeObserver: Any? = null

    init {
        val interval = CMTimeMakeWithSeconds(0.25, 1000)
        timeObserver = avPlayer.addPeriodicTimeObserverForInterval(
            interval = interval,
            queue = dispatch_get_main_queue()
        ) { cmTime ->
            updatePlaybackState(cmTime)
        }
    }

    private fun updatePlaybackState(time: CValue<CMTime>) {
        val item = avPlayer.currentItem
        val positionSec = CMTimeGetSeconds(time)

        val durationSec = item?.duration?.let { d ->
            val s = CMTimeGetSeconds(d)
            if (s.isFinite()) s else 0.0
        } ?: 0.0

        val bufferedSec = item?.loadedTimeRanges?.firstOrNull()?.let { value ->
            (value as NSValue).CMTimeRangeValue.useContents {
                val startSec = CMTimeGetSeconds(this.start.readValue())
                val durSec = CMTimeGetSeconds(this.duration.readValue())
                if (startSec.isFinite() && durSec.isFinite()) startSec + durSec else 0.0
            }
        } ?: 0.0

        val status = avPlayer.timeControlStatus
        val isPlaying = status == AVPlayerTimeControlStatusPlaying
        val isBuffering = status == AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
        val isReady = item?.status == AVPlayerItemStatusReadyToPlay

        val errorMsg = item?.error?.localizedDescription
            ?: avPlayer.error?.localizedDescription

        _state.update {
            it.copy(
                isPlaying = isPlaying,
                isBuffering = isBuffering,
                isReady = isReady,
                positionMs = (positionSec * 1000).toLong().coerceAtLeast(0L),
                durationMs = (durationSec * 1000).toLong().coerceAtLeast(0L),
                bufferedMs = (bufferedSec * 1000).toLong().coerceAtLeast(0L),
                isMuted = avPlayer.muted,
                error = errorMsg
            )
        }
    }

    actual fun setSource(url: String, autoPlay: Boolean, startPositionMs: Long) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        val item = AVPlayerItem(uRL = nsUrl)
        avPlayer.replaceCurrentItemWithPlayerItem(item)

        if (startPositionMs > 0) {
            avPlayer.seekToTime(CMTimeMakeWithSeconds(startPositionMs / 1000.0, 1000))
        }
        if (autoPlay) avPlayer.play()
    }

    actual fun play() { avPlayer.play() }
    actual fun pause() { avPlayer.pause() }

    actual fun seekTo(ms: Long) {
        avPlayer.seekToTime(CMTimeMakeWithSeconds(ms / 1000.0, 1000))
    }

    actual fun setMuted(muted: Boolean) {
        avPlayer.setMuted(muted)
    }

    actual fun release() {
        avPlayer.pause()
        avPlayer.replaceCurrentItemWithPlayerItem(null)
        _state.update { PlayerState() }
    }

    // Sof release uchun (singleton scope tugaganda)
    fun dispose() {
        timeObserver?.let { avPlayer.removeTimeObserver(it) }
        timeObserver = null
        release()
    }
}
