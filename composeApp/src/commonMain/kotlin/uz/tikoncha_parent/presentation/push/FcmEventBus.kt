package uz.tikoncha_parent.presentation.push

import kotlinx.serialization.json.Json
import uz.tikoncha_parent.domain.model.AppBlock
import uz.tikoncha_parent.domain.model.FcmPayload
import uz.tikoncha_parent.domain.model.MessageBlock
import uz.tikoncha_parent.domain.model.NewsBlock
import uz.tikoncha_parent.domain.model.TodoBlock

object FcmEventBus {
    private val listener = mutableSetOf<FcmEventListener>()
    fun add(l: FcmEventListener) { listener += l }
    fun remove(l: FcmEventListener) { listener -= l }

    fun emitApp(app: AppBlock) = listener.forEach { it.onAppRule(app) }
    fun emitTodo(todo: TodoBlock, title: String?, message: String?) = listener.forEach { it.onTodo(todo, title, message) }
    fun emitNews(news: NewsBlock, title: String?, message: String?) = listener.forEach { it.onNews(news, title, message) }
    fun emitChat(msg: MessageBlock, title: String?, message: String?) = listener.forEach { it.onChat(msg, title, message) }
    fun emitGeneral(title: String?, message: String?) = listener.forEach { it.onGeneral(title, message) }


}

interface FcmEventListener {
    fun onAppRule(app: AppBlock) {}
    fun onTodo(todo: TodoBlock, title: String?, message: String?) {}
    fun onNews(news: NewsBlock, title: String?, message: String?) {}
    fun onChat(msg: MessageBlock, title: String?, message: String?) {}
    fun onGeneral(title: String?, message: String?) {}
}

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}
fun parseFcmPayload(raw: String?): FcmPayload? {
    if (raw.isNullOrBlank()) return null
    return runCatching { json.decodeFromString(FcmPayload.serializer(), raw) }.getOrNull()
}