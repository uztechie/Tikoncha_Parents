package uz.tikoncha_parent.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.remote.model.NewsDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.NewsRepository
import uz.tikoncha_parent.domain.use_case.NewsUseCase

class NotificationViewModel (
    private val repository: NewsRepository,
    private val getNewsUseCase: NewsUseCase
): ScreenModel {

    private var newsJob: Job? = null

    private val _state = MutableStateFlow(NotificationUiState())
    val state = _state.asStateFlow()

    init {
        screenModelScope.launch {
            NotificationRefreshEventBus.refresh.collect { load() }
        }
    }

    fun load() {
        _state.update { it.copy(loading = true, error = null) }
        newsJob?.cancel()

        newsJob = screenModelScope.launch {
            when (val result = getNewsUseCase()) {
                is Resource.Loading -> Unit
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, error = result.message) }
                }
                is Resource.Success -> {
                    val flat: List<NewsDto> = result.data.flatMap { it.items }

                    val sorted = flat.sortedByDescending { dto ->
                        val primary = dto.modified_at.ifBlank { dto.created_at }
                        DateTimeUtil.toMillisUtc(primary)
                    }

                    _state.update {
                        it.copy(loading = false, items = sorted, error = null)
                    }

                    val unreadCount = sorted.count { !it.is_read }
                    NotificationUnreadEventBus.tryEmit(
                        NotificationUnreadEventBus.Event.SyncCount(unreadCount)
                    )

                }
            }
        }
    }

    fun markReadOptimistic(id: Long) {
        val current = _state.value.items
        val target = current.find { it.id == id } ?: return
        if (target.is_read) return

        _state.update {
            it.copy(
                items = current.map { item ->
                    if (item.id == id) item.copy(is_read = true) else item
                }
            )
        }

        screenModelScope.launch {
            runCatching { repository.markNewsRead(id) }
                .onSuccess {
                    NotificationUnreadEventBus.tryEmit(
                        NotificationUnreadEventBus.Event.MarkOneRead(id)
                    )
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            items = current.map { item ->
                                if (item.id == id) item.copy(is_read = false) else item
                            }
                        )
                    }
                }
        }
    }

    fun markAllReadOptimistic(){
        val previouslyUnread = _state.value.items.filter { !it.is_read }.map { it.id }.toSet()
        if (previouslyUnread.isEmpty()) return

        _state.update {
            it.copy(
                items = it.items.map { item ->
                    item.copy(is_read = true)
                }
            )

        }

        screenModelScope.launch {
            runCatching { repository.markAllNewsRead() }
                .onSuccess {
                    NotificationUnreadEventBus.tryEmit(
                        NotificationUnreadEventBus.Event.MarkAllRead
                    )
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            items = it.items.map { item ->
                                if (item.id in previouslyUnread) item.copy(is_read = false) else item
                            }
                        )
                    }
                }
        }
    }
}