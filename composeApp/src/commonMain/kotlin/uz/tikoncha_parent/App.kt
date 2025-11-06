package uz.tikoncha_parent

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.platform.AppEnvironment
import uz.tikoncha_parent.presentation.splash.SplashScreen


import uz.tikoncha_parent.presentation.profile.language.LanguageController
import uz.tikoncha_parent.presentation.profile.language.LocalLanguageController
import uz.tikoncha_parent.ui.theme.BarConfig
import uz.tikoncha_parent.ui.theme.LocalBarsConfig
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.PlatformThemeBridge
import uz.tikoncha_parent.ui.theme.ThemeController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.sulgik.mapkit.MapKit
import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.presentation.chat.ChatMessageScreen
import uz.tikoncha_parent.presentation.chat.ChatScreen
import uz.tikoncha_parent.presentation.home.HomeScreen
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.push.DeepLinkEffect
import uz.tikoncha_parent.presentation.push.FcmEventListenerEffect
import uz.tikoncha_parent.presentation.push.PendingDeepLinks
import uz.tikoncha_parent.presentation.push.navigateByDeepLink
import uz.tikoncha_parent.presentation.task.TaskScreen
import uz.tikoncha_parent.ui.theme.ThemeMode


@Composable
@Preview
fun App() {

    println("APP screem")


    val langController = remember { LanguageController() }
    val barsConfig = remember { mutableStateOf(BarConfig()) }

    val mode by ThemeController.mode.collectAsState(initial = ThemeMode.LIGHT)

    val inPreview = androidx.compose.ui.platform.LocalInspectionMode.current
    if (!inPreview) {
        SideEffect { PlatformThemeBridge.onModeChanged(mode) }
    }

    FcmEventListenerEffect(
        onApp = { app ->
            // AppRuleEntity ga map qilib saqlash va hokazo
        },
        onTodo = { todo, title, message ->
            // Floating overlay yoki notification
        },
        onNews = { news, title, message ->
            // NewsRefreshEventBus.notifyRefresh(), notification ko'rsatish, deep link
        },
        onChat = { msg, title, message ->
            // createChatPendingIntent(...), ChatUnreadEventBus.tryEmit(...)
        },
        onGeneral = { title, message ->
            // Oddiy bildirish noma
        }
    )

    AppEnvironment {

        CompositionLocalProvider(
            LocalLanguageController provides langController,
            LocalBarsConfig provides barsConfig
        )
        {
            TikonchaParentTheme(
                mode = mode
            ) {
                val cfg = barsConfig.value

                Surface(
                    modifier = Modifier
                        .then(if (cfg.paddingEnabled) Modifier.statusBarsPadding() else Modifier)
                        .then(if (cfg.paddingEnabled) Modifier.navigationBarsPadding() else Modifier)
                ) {

                    val pendingLinks = remember { PendingDeepLinks.drain() }



                    val initialScreen = remember(pendingLinks) {
                        deepLinkToInitialScreenOrLauncher(pendingLinks.firstOrNull())
                    }


                    val initialStack: List<Screen> = remember(pendingLinks) {
                        initialStackFor(pendingLinks.firstOrNull()) ?: emptyList()
                    }

                    if (initialStack.isNotEmpty()) {
                        val first = initialStack.first()
                        val rest  = initialStack.drop(1)
                        Navigator(first) { nav ->
                            CurrentScreen()
                            DeepLinkEffect(nav)

                            // Stack’ni to‘liq tiklash
                            LaunchedEffect(rest) {
                                rest.forEach { screen -> nav.push(screen) }
                            }

                            // Pending’larni navbatdan o‘tkazish
                            LaunchedEffect(pendingLinks) {
                                pendingLinks.drop(1).forEach { link -> navigateByDeepLink(nav, link) }
                            }
                        }
                    } else {
                        Navigator(SplashScreen()) { nav ->
                            CurrentScreen()
                            DeepLinkEffect(nav)
                        }
                    }
                }
            }
        }
    }
}

private fun initialStackFor(link: DeepLink?): List<Screen>? = when (link) {
    is DeepLink.Chat -> listOf(
        HomeScreen(),
        ChatScreen(),
        ChatMessageScreen(
            chatId = link.chatId,
            chatAvatar = "",
            chatTitle = link.chatTitle ?: "",
            chatType = ChatType.NONE
        )
    )
    is DeepLink.News -> listOf(HomeScreen(), NotificationScreen())
    is DeepLink.Todo -> listOf(HomeScreen(), TaskScreen())
    else -> null
}

fun deepLinkToInitialScreenOrLauncher(link: DeepLink?): Screen =
    when (link) {
        is DeepLink.Chat -> ChatScreen()
        is DeepLink.News -> NotificationScreen()
        is DeepLink.Todo -> TaskScreen()
        else -> SplashScreen()
    }

fun initMapKit() {
    val MAP_KEY: String = "21612db3-4394-4fde-b579-d2e7a1f9afa3"
    MapKit.setApiKey(MAP_KEY)
}


