package uz.tikoncha_parent.domain.model 

sealed class DeepLink {
    data class Chat(val chatId: String, val chatTitle: String? = null, val text: String? = null): DeepLink()
    data class News(val newsId: String): DeepLink()
    data class Todo(val taskId: String): DeepLink()
    data object ChildRequest: DeepLink()
    data class General(val title: String? = null, val message: String? = null): DeepLink()
}