package uz.tikoncha_parent

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
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
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.chat.chat_list.ChatScreen
import uz.tikoncha_parent.presentation.chat.chat_room.ChatRoomScreen
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.presentation.new_home.NewHomeScreen
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestScreen
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

    val restartKey by AppRestartBus.key.collectAsState()

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


            val disposeBehavior = remember {
                NavigatorDisposeBehavior(
                    disposeNestedNavigators = true, // ✅ nested navigatorlar ham dispose bo‘lsin
                    disposeSteps = true
                )
            }

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



                    val initialStack: List<Screen> = remember(pendingLinks) {
                        initialStackFor(pendingLinks.firstOrNull()) ?: emptyList()
                    }

                    Logger.d("Appppp", "pendingLinks=$pendingLinks")
                    Logger.d("Appppp", "initialStack=$initialStack")


                    key(restartKey){
                        if (initialStack.isNotEmpty()) {
                            val first = initialStack.first()
                            val rest  = initialStack.drop(1)
                            Navigator(first, disposeBehavior) { nav ->
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
                            Navigator(SplashScreen(), disposeBehavior) { nav ->
                                CurrentScreen()
                                DeepLinkEffect(nav)
                            }
                        }
                    }

                }
            }
        }
    }
}

private fun initialStackFor(link: DeepLink?): List<Screen>? = when (link) {
    is DeepLink.Chat -> listOf(
        NewHomeScreen(),
        ChatScreen(),
        ChatRoomScreen(
            chatId = link.chatId,
            chatAvatar = "",
            chatTitle = link.chatTitle ?: "",
            chatType = ChatType.NONE
        )
    )
    is DeepLink.News -> listOf(NewHomeScreen(), NotificationScreen())
    is DeepLink.Todo -> listOf(NewHomeScreen(), TaskScreen())
    DeepLink.ChildRequest -> listOf(NewHomeScreen(), ParentRequestScreen())
    else -> null
}

fun initMapKit() {
    val MAP_KEY: String = "21612db3-4394-4fde-b579-d2e7a1f9afa3"
    MapKit.setApiKey(MAP_KEY)
}


