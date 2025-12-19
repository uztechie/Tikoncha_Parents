package uz.tikoncha_parent.presentation.push

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.chat.ChatMessageScreen
import uz.tikoncha_parent.presentation.chat.ChatScreen
import uz.tikoncha_parent.presentation.statistic.StatisticScreen
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestScreen
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.task.TaskScreen

@Composable
fun DeepLinkEffect(navigator: Navigator) {
    val handled = remember { mutableStateSetOf<String>() }


    DisposableEffect(navigator) {
        val l = object : DeepLinkListener {
            override fun onOpen(link: DeepLink) {
                val key = link.dedupeKey()
                Logger.d("DeepLinkEffect", "key=$key")
                if (handled.add(key)){
                    navigateByDeepLink(navigator, link)
                }

            }
        }
        DeepLinkBus.add(l)
        onDispose { DeepLinkBus.remove(l) }
    }

    val top = navigator.lastItem
    LaunchedEffect(top) {
        if (top is StatisticScreen) handled.clear()
    }

}



private fun DeepLink.dedupeKey(): String = when (this) {
    is DeepLink.Chat -> "chat:$chatId"
    is DeepLink.News -> "news"           // xohlasangiz id bo‘lsa qo‘ying
    is DeepLink.Todo -> "todo"
    is DeepLink.General -> "general"
    is DeepLink.ChildRequest -> "child_request"
    else -> "none"
}

fun navigateByDeepLink(navigator: Navigator, link: DeepLink) {
    when (link) {
        is DeepLink.Chat -> navigator.push(
            listOf(
                StatisticScreen(),
                ChatScreen(),
                ChatMessageScreen(
                    chatId = link.chatId,
                    chatAvatar = "",
                    chatTitle = link.chatTitle ?: "",
                    chatType = ChatType.NONE
                )
            )
        )

        is DeepLink.News -> navigator.push(NotificationScreen())
        is DeepLink.Todo -> navigator.push(TaskScreen())
        is DeepLink.General -> {
            // xohlasangiz umumiy "Inbox" yoki dialog ko‘rsating
        }

        DeepLink.ChildRequest -> {
            Logger.d("DeepLinkEffect", "request ChildRequest")
            navigator.push(ParentRequestScreen())
        }

        else -> {

        }
    }
}