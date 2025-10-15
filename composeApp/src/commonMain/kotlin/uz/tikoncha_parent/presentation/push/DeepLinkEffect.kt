package uz.tikoncha_parent.presentation.push

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.presentation.chat.ChatScreen

@Composable
fun DeepLinkEffect(navigator: Navigator) {
    DisposableEffect(navigator) {
        val l = object : DeepLinkListener {
            override fun onOpen(link: DeepLink) {
                navigateByDeepLink(navigator, link)
            }
        }
        DeepLinkBus.add(l)
        onDispose { DeepLinkBus.remove(l) }
    }

    fun navigateByDeepLink(navigator: Navigator, link: DeepLink) {
        when (link) {
            is DeepLink.Chat -> navigator.push(
                ChatScreen(
                    link.chatId,
                    link.chatTitle,
                    link.text
                )
            )
            is DeepLink.News -> navigator.push(News(link.newsId))
            is DeepLink.Todo -> navigator.push(TodoDetailsScreen(link.taskId))
            is DeepLink.General -> {
                // xohlasangiz umumiy "Inbox" yoki dialog ko‘rsating
            }
        }
    }
}