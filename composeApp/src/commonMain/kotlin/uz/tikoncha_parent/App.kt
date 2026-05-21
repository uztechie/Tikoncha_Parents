package uz.tikoncha_parent

import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import uz.tikoncha_parent.platform.AppEnvironment
import uz.tikoncha_parent.presentation.splash.SplashScreen


import uz.tikoncha_parent.presentation.profile.language.LanguageController
import uz.tikoncha_parent.presentation.profile.language.LocalLanguageController
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.PlatformThemeBridge
import uz.tikoncha_parent.ui.theme.ThemeController
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.dialog_warning
import tikoncha_parents.composeapp.generated.resources.login_qilish
import tikoncha_parents.composeapp.generated.resources.ok
import tikoncha_parents.composeapp.generated.resources.token_eskirgan
import tikoncha_parents.composeapp.generated.resources.tokenni_yangilash_uchun
import uz.tikoncha_parent.data.remote.AuthEvent
import uz.tikoncha_parent.data.remote.AuthEventBus
import uz.tikoncha_parent.domain.model.DeepLink
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.chat.chat_list.ChatScreen
import uz.tikoncha_parent.presentation.chat.chat_room.ChatRoomScreen
import uz.tikoncha_parent.presentation.login.LoginScreen
import uz.tikoncha_parent.presentation.map.MapKitInitializer
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.presentation.navigation.SwipeBackContent
import uz.tikoncha_parent.presentation.new_home.NewHomeScreen
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestScreen
import uz.tikoncha_parent.presentation.notification.NotificationScreen
import uz.tikoncha_parent.presentation.push.DeepLinkEffect
import uz.tikoncha_parent.presentation.push.FcmEventListenerEffect
import uz.tikoncha_parent.presentation.push.PendingDeepLinks
import uz.tikoncha_parent.presentation.push.navigateByDeepLink
import uz.tikoncha_parent.presentation.task.TaskScreen


@Composable
@Preview
fun App() {

    println("APP screem")


    val langController = remember { LanguageController() }

    val mode by ThemeController.mode.collectAsState()

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
            LocalLanguageController provides langController
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

                Surface {

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
                                SwipeBackContent(nav)
                                DeepLinkEffect(nav)
                                AuthEventListener(nav)
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
                                SwipeBackContent(navigator = nav)
                                DeepLinkEffect(nav)
                                AuthEventListener(nav)
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
    try {
        val MAP_KEY: String = "21612db3-4394-4fde-b579-d2e7a1f9afa3"
        MapKitInitializer.initialize(MAP_KEY)
        println("✅ MAPKIT_OK initialized")
    } catch (e: Throwable) {
        println("❌ MAPKIT_ERROR: ${e::class.simpleName}: ${e.message}")
        e.printStackTrace()
    }

}


@Composable
private fun AuthEventListener(navigator: Navigator) {

    var showDialog by remember { mutableStateOf(false) }

    CustomDialog(
        title = stringResource(Res.string.token_eskirgan),
        message = stringResource(Res.string.tokenni_yangilash_uchun),
        painter = painterResource(Res.drawable.dialog_failed),
        buttonText = stringResource(Res.string.login_qilish),
        buttonText2 = stringResource(Res.string.bekor_qilish),
        showCloseButton = false,
        show = showDialog,
        onButtonClick = {
            showDialog = false
            if (navigator.lastItem !is LoginScreen) {
                navigator.replaceAll(LoginScreen())
            }
        },
        onDismiss = {
            showDialog = false
        }
    )

    LaunchedEffect(navigator) {
        AuthEventBus.events.collect { event ->
            when (event) {
                AuthEvent.SessionExpired -> {
                    showDialog = true
                }
            }
        }
    }
}

