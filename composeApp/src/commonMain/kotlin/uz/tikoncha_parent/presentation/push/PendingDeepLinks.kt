package uz.tikoncha_parent.presentation.push

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import uz.tikoncha_parent.domain.model.DeepLink

object PendingDeepLinks {

    // Simple thread-safe-ish buffer via atomic StateFlow updates
    private val _buf = MutableStateFlow<List<DeepLink>>(emptyList())

    fun enqueue(link: DeepLink) {
        _buf.update { it + link }
    }

    /**
     * Drain once (take snapshot + clear)
     */
    fun drain(): List<DeepLink> {
        val current = _buf.value
        if (current.isEmpty()) return emptyList()
        _buf.value = emptyList()
        return current
    }
}
