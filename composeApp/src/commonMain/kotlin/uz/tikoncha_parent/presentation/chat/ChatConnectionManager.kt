package uz.tikoncha_parent.presentation.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import uz.tikoncha_parent.domain.use_case.chat.ConnectChatWebSocketUseCase
import uz.tikoncha_parent.domain.use_case.chat.DisconnectChatWebSocketUseCase
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class ChatConnectionManager(
    private val connectUseCase: ConnectChatWebSocketUseCase,
    private val disConnectUseCase: DisconnectChatWebSocketUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val tags: MutableSet<String> = mutableSetOf()
    private var releaseJob: Job? = null




    fun acquire(tag: String){
        releaseJob?.cancel()
        if (tags.add(tag) && tags.size == 1){
            connectUseCase()
        }
    }

    fun release(tag: String){
        tags.remove(tag)
        if (tags.isEmpty()){
            releaseJob?.cancel()
            releaseJob = scope.launch {
                delay(2000)
                if (tags.isEmpty()){
                    disConnectUseCase()
                }
            }
        }
    }
}