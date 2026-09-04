package uz.tikoncha_parent.presentation.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.tikoncha_parent.domain.repository.ChatRepository

class ChatConnectionManager(
    private val repository: ChatRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val tags: MutableSet<String> = mutableSetOf()
    private var releaseJob: Job? = null

    fun acquire(tag: String) {
        releaseJob?.cancel()
        if (tags.add(tag) && tags.size == 1) {
            repository.connect()
        }
    }

    fun release(tag: String) {
        tags.remove(tag)
        if (tags.isEmpty()) {
            releaseJob?.cancel()
            releaseJob = scope.launch {
                delay(2000)
                if (tags.isEmpty()) {
                    repository.disconnect()
                }
            }
        }
    }
}