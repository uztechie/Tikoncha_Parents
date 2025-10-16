package uz.tikoncha_parent.presentation.push

import android.content.Intent
import android.net.Uri
import uz.tikoncha_parent.domain.model.DeepLink

object AndroidDeepLinkParser {
    fun parse(intent: Intent?): DeepLink? {
        val uri: Uri = intent?.data ?: return null
        return when (uri.host) {
            "chat" -> {
                val id = uri.getQueryParameter("chatId") ?: return null
                val title = uri.getQueryParameter("chatTitle")
                val type = uri.getQueryParameter("chatType")
                DeepLink.Chat(id, title, type)
            }
            "news" -> {
                val id = uri.getQueryParameter("newsId") ?: return null
                DeepLink.News(id)
            }
            "todo" -> {
                val id = uri.getQueryParameter("taskId") ?: return null
                DeepLink.Todo(id)
            }
            else -> null
        }
    }
}
