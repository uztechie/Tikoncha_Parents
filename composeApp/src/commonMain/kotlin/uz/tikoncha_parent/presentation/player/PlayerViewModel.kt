package uz.tikoncha_parent.presentation.player

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.data.player.PlayerEngine
import uz.tikoncha_parent.domain.repository.PlayerRepository

class PlayerScreenModel(
    val engine: PlayerEngine,
    private val repository: PlayerRepository,
) : ScreenModel {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        screenModelScope.launch {
            repository.observeState().collect { s ->
                _uiState.update {
                    it.copy(
                        isPlaying = s.isPlaying,
                        isMuted = s.isMuted,
                        isLoading = s.isBuffering,
                        isReady = s.isReady,
                        positionMs = s.positionMs,
                        durationMs = s.durationMs,
                        bufferedMs = s.bufferedMs,
                        error = s.error
                    )
                }
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.Load -> {
                _uiState.update {
                    it.copy(error = null, shouldClose = false, videoUrl = event.url)
                }
                repository.setSource(event.url, event.autoPlay, event.startPositionMs)
            }
            PlayerEvent.TogglePlayPause -> repository.togglePlayPause()
            is PlayerEvent.SeekTo       -> repository.seekTo(event.ms)
            is PlayerEvent.SeekBy       -> repository.seekBy(event.deltaMs)
            PlayerEvent.ToggleMute      -> repository.toggleMute()
            PlayerEvent.Close           -> _uiState.update { it.copy(shouldClose = true) }
            PlayerEvent.Release         -> repository.release()
            PlayerEvent.ClearError      -> _uiState.update { it.copy(error = null) }
        }
    }

    fun consumeClose() {
        _uiState.update { it.copy(shouldClose = false) }
    }

    override fun onDispose() {
        // Screen pop bo'lganda avtomatik chaqiriladi — Android + iOS ikkalasida
        repository.release()
        super.onDispose()
    }
}