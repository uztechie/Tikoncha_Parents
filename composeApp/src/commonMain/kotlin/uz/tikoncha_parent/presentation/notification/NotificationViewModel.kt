package uz.tikoncha_parent.presentation.notification

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.repository.NewsRepository

class NotificationViewModel (
    private val repository: NewsRepository
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
            when (val res = repository.fetchNews()) {
                is Outcome.Failure -> _state.update {
                    it.copy(loading = false, error = res)
                }

                is Outcome.Success -> {
                    val sorted = res.data.sortedByDescending { news ->
                        if (news.modifiedAt > 0L) news.modifiedAt else news.createdAt
                    }

                    _state.update { it.copy(loading = false, items = sorted, error = null) }

                    val unreadCount = sorted.count { !it.isRead }
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
        if (target.isRead) return

        _state.update {
            it.copy(
                items = current.map { item ->
                    if (item.id == id) item.copy(isRead = true) else item
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
                                if (item.id == id) item.copy(isRead = false) else item
                            }
                        )
                    }
                }
        }
    }

    fun markAllReadOptimistic(){
        val previouslyUnread = _state.value.items.filter { !it.isRead }.map { it.id }.toSet()
        if (previouslyUnread.isEmpty()) return

        _state.update {
            it.copy(
                items = it.items.map { item ->
                    item.copy(isRead = true)
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
                                if (item.id in previouslyUnread) item.copy(isRead = false) else item
                            }
                        )
                    }
                }
        }
    }
}