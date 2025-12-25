package uz.tikoncha_parent.presentation.push

//import kotlinx.atomicfu.locks.SynchronizedObject
//import kotlinx.atomicfu.locks.synchronized
import uz.tikoncha_parent.domain.model.DeepLink

object PendingDeepLinks {
//    private val lock = SynchronizedObject()
//    private val buf = ArrayDeque<DeepLink>()

    fun enqueue(link: DeepLink) = {}

    fun drain(): List<DeepLink> = emptyList()

//    fun enqueue(link: DeepLink) = synchronized(lock) {
//        buf.addLast(link)
//    }

//    fun drain(): List<DeepLink> = synchronized(lock) {
//        if (buf.isEmpty()) emptyList()
//        else buildList { addAll(buf); buf.clear() }
//    }
}
