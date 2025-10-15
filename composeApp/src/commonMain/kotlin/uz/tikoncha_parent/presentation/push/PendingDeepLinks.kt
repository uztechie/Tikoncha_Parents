package uz.tikoncha_parent.presentation.push

import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.locks.synchronized
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import uz.tikoncha_parent.domain.model.DeepLink

@OptIn(InternalAPI::class)
object PendingDeepLinks {
    private val lock = SynchronizedObject()
    private val buf = ArrayDeque<DeepLink>()

    fun enqueue(link: DeepLink) = synchronized(lock){
        buf.add(link)
    }

    fun drain(): List<DeepLink> = synchronized(lock) {
        if (buf.isEmpty()) return emptyList()
        val out = buf.toList()
        buf.clear()
        out
    }
}